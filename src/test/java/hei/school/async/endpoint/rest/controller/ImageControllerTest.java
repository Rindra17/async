package hei.school.async.endpoint.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hei.school.async.service.ImageUploadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ImageController.class)
class ImageControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ImageUploadService imageUploadService;

  @Test
  void upload_Success() throws Exception {
    // Given
    MockMultipartFile file =
        new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG_VALUE, "content".getBytes());
    doNothing().when(imageUploadService).uploadImage(anyString(), any());

    // When & Then
    mockMvc
        .perform(multipart("/upload").file(file).param("email", "test@example.com"))
        .andExpect(status().isOk());
  }
}
