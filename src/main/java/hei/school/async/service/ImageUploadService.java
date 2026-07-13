package hei.school.async.service;

import hei.school.async.endpoint.event.EventProducer;
import hei.school.async.endpoint.event.model.ProcessImageEvent;
import hei.school.async.file.bucket.BucketComponent;
import hei.school.async.repository.UploadRepository;
import hei.school.async.repository.model.JUpload;
import hei.school.async.endpoint.rest.controller.validator.ImageValidator;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageUploadService {
  private final UploadRepository uploadRepository;
  private final BucketComponent bucketComponent;
  private final EventProducer<ProcessImageEvent> eventProducer;
  private final ImageValidator imageValidator;

  @SneakyThrows
  public void uploadImage(String email, MultipartFile file) {
    imageValidator.validateEmail(email);
    imageValidator.validateImage(file);

    String extension = getFileExtension(file.getOriginalFilename());
    String bucketKey = "originals/" + UUID.randomUUID() + "." + extension;

    File tempFile = File.createTempFile("upload-", "." + extension);
    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
      fos.write(file.getBytes());
    }

    bucketComponent.upload(tempFile, bucketKey);

    var uploadMetadata = JUpload.builder()
        .id(UUID.randomUUID())
        .fileName(bucketKey)
        .email(email)
        .createdAt(java.time.Instant.now())
        .build();
    uploadRepository.save(uploadMetadata);

    var event = ProcessImageEvent.builder()
        .imageId(uploadMetadata.getId())
        .bucketKey(bucketKey)
        .email(email)
        .extension(extension)
        .build();
    eventProducer.accept(List.of(event));

    tempFile.delete();
  }

  private String getFileExtension(String fileName) {
    if (fileName == null)
      return "jpg";
    int lastDotIndex = fileName.lastIndexOf('.');
    return (lastDotIndex == -1) ? "jpg" : fileName.substring(lastDotIndex + 1);
  }
}
