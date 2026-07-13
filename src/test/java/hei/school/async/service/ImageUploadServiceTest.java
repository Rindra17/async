package hei.school.async.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import hei.school.async.endpoint.event.EventProducer;
import hei.school.async.endpoint.event.model.ProcessImageEvent;
import hei.school.async.endpoint.rest.controller.validator.ImageValidator;
import hei.school.async.file.bucket.BucketComponent;
import hei.school.async.repository.UploadRepository;
import hei.school.async.repository.model.JUpload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class ImageUploadServiceTest {

  @Mock private UploadRepository uploadRepository;
  @Mock private BucketComponent bucketComponent;
  @Mock private EventProducer<ProcessImageEvent> eventProducer;
  @Mock private ImageValidator imageValidator;

  @InjectMocks private ImageUploadService imageUploadService;

  private MockMultipartFile mockFile;
  private final String email = "test@example.com";

  @BeforeEach
  void setUp() {
    mockFile =
        new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
  }

  @Test
  void uploadImage_Success() {
    // Given
    doNothing().when(imageValidator).validateEmail(anyString());
    doNothing().when(imageValidator).validateImage(any());

    // When
    imageUploadService.uploadImage(email, mockFile);

    // Then
    verify(imageValidator).validateEmail(email);
    verify(imageValidator).validateImage(mockFile);
    verify(bucketComponent).upload(any(), anyString());
    verify(uploadRepository).save(any(JUpload.class));
    verify(eventProducer).accept(anyList());
  }

  @Test
  void uploadImage_InvalidImage_ThrowsException() {
    // Given
    doThrow(new IllegalArgumentException("Invalid format"))
        .when(imageValidator)
        .validateImage(any());

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          imageUploadService.uploadImage(email, mockFile);
        });

    verify(uploadRepository, never()).save(any());
  }
}
