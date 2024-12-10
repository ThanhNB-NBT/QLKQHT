package common;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class ImageUtils {

    private static final String DEFAULT_AVATAR_PATH = "assets/img/user.jpg";
    private static final String UPLOAD_DIRECTORY = "D:/eclipse-workspace/QLKQHT/src/main/webapp/assets/img/profile";

    public static String processAvatar(HttpServletRequest request, String defaultAvatar) throws IOException, ServletException {
        try {
            Part avatarPart = request.getPart("avatar");

            if (avatarPart != null && avatarPart.getSize() > 0) {
                return processAvatarFile(avatarPart, UPLOAD_DIRECTORY, true, 150, 150);
            }
        } catch (Exception e) {
            // Log lỗi nếu cần thiết
            e.printStackTrace();
        }
        return defaultAvatar;
    }

    public static String processAvatar(HttpServletRequest request) throws IOException, ServletException {
        return processAvatar(request, DEFAULT_AVATAR_PATH);
    }

    private static String processAvatarFile(Part filePart, String uploadDir, boolean resize, int width, int height) throws IOException {

        if (filePart == null || filePart.getSize() <= 0) {
            return DEFAULT_AVATAR_PATH;
        }

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + "_" + filePart.getSubmittedFileName();
        String outputPath = uploadDir + File.separator + fileName;

        filePart.write(outputPath);

        if (resize) {

            BufferedImage originalImage = ImageIO.read(new File(outputPath));
            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bufferedResizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            bufferedResizedImage.getGraphics().drawImage(resizedImage, 0, 0, null);

            ImageIO.write(bufferedResizedImage, "png", new File(outputPath));
        }
        return "assets/img/profile/" + fileName;
    }
}
