package common;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;
import jakarta.servlet.http.Part;

public class ImageUtils {

    public static String processAvatar(Part filePart, String uploadDir, boolean resize, int width, int height) throws IOException {
        if (filePart == null || filePart.getSize() <= 0) {
            // Không upload file, trả về avatar mặc định
            return "assets/img/user.jpg";
        }

        // Đảm bảo thư mục tồn tại
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Tạo tên file duy nhất
        String fileName = UUID.randomUUID().toString() + "_" + filePart.getSubmittedFileName();
        String outputPath = uploadDir + File.separator + fileName;

        // Lưu file gốc
        filePart.write(outputPath);

        if (resize) {
            // Resize ảnh
            BufferedImage originalImage = ImageIO.read(new File(outputPath));
            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bufferedResizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            bufferedResizedImage.getGraphics().drawImage(resizedImage, 0, 0, null);

            // Ghi đè ảnh resized lên file gốc
            ImageIO.write(bufferedResizedImage, "png", new File(outputPath));
        }

        // Trả về đường dẫn tương đối để lưu vào cơ sở dữ liệu
        return "assets/img/profile/" + fileName;
    }
}
