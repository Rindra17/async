package hei.school.async.endpoint.rest.controller;

import hei.school.async.service.ImageUploadService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class ImageController {
  private final ImageUploadService uploadService;

  @PostMapping("/upload")
  public ResponseEntity<?> upload(@RequestParam String email, @RequestParam MultipartFile image) {
    uploadService.uploadImage(email, image);
    return ResponseEntity.ok(
        "Image uploaded successfully. You will receive the processed image via email.");
  }
}
