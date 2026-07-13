package hei.school.async.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hei.school.async.repository.model.JUpload;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UploadRepositoryTest {

  @Mock private UploadRepository uploadRepository;

  @Test
  void saveAndFind_Success() {
    // Given
    UUID id = UUID.randomUUID();
    JUpload upload =
        JUpload.builder()
            .id(id)
            .fileName("test.jpg")
            .email("test@example.com")
            .createdAt(Instant.now())
            .build();

    when(uploadRepository.save(any(JUpload.class))).thenReturn(upload);
    when(uploadRepository.findById(id)).thenReturn(Optional.of(upload));

    // When
    uploadRepository.save(upload);
    Optional<JUpload> found = uploadRepository.findById(id);

    // Then
    assertTrue(found.isPresent());
    assertEquals("test.jpg", found.get().getFileName());
    verify(uploadRepository, times(1)).save(upload);
  }
}
