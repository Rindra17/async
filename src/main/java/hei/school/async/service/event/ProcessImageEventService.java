package hei.school.async.service.event;

import hei.school.async.endpoint.event.model.ProcessImageEvent;
import hei.school.async.file.bucket.BucketComponent;
import hei.school.async.mail.Email;
import hei.school.async.mail.Mailer;
import hei.school.async.service.ImageProcessingService;
import jakarta.mail.internet.InternetAddress;
import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProcessImageEventService implements Consumer<ProcessImageEvent> {
  private final BucketComponent bucketComponent;
  private final ImageProcessingService imageProcessingService;
  private final Mailer mailer;

  @SneakyThrows
  @Override
  public void accept(ProcessImageEvent event) {
    File originalFile = bucketComponent.download(event.getBucketKey());

    try {
      File bwFile = imageProcessingService.convertToBlackAndWhite(originalFile, event.getExtension());

      String bwBucketKey = "processed/" + event.getImageId() + "_bw." + event.getExtension();
      bucketComponent.upload(bwFile, bwBucketKey);

      String presignedUrl = bucketComponent.presign(bwBucketKey, Duration.ofHours(1)).toString();

      InternetAddress recipient = new InternetAddress(event.getEmail());
       var email = new Email(
           recipient,
           List.of(),
           List.of(),
           "Upload Successful",
           "Hello,\n\nYour image has been processed successfully. You can download your greyscale image using the following link:\n\n" + presignedUrl + "\n\nBest regards,\nYour Image Service",
           List.of());
      mailer.accept(email);

      bwFile.delete();
    } finally {
      originalFile.delete();
    }
  }
}
