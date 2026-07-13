package hei.school.async.service;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class ImageProcessingService {

  @SneakyThrows
  public File convertToBlackAndWhite(File inputFile, String format) {
    BufferedImage sourceImage = ImageIO.read(inputFile);
    if (sourceImage == null) {
      throw new IOException("Could not read image file: " + inputFile.getName());
    }

    BufferedImage resultImage =
        new BufferedImage(
            sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

    ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
    op.filter(sourceImage, resultImage);

    File outputFile = File.createTempFile("bw-", "." + format);
    ImageIO.write(resultImage, format, outputFile);

    return outputFile;
  }
}
