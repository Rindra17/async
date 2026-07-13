package hei.school.async.endpoint.rest.controller.validator;

import hei.school.async.exception.AppException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageValidator {

  public void validateImage(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new AppException("Image file is required");
    }

    String contentType = file.getContentType();
    if (contentType == null ||
        (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
      throw new AppException("Only .jpeg and .png images are accepted");
    }
  }

  public void validateEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new AppException("Email address is required");
    }
    if (!email.contains("@") || !email.contains(".")) {
      throw new AppException("Invalid email format");
    }
  }
}
