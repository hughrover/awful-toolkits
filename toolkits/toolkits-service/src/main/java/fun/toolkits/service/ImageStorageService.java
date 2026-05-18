package fun.toolkits.service;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageStorageService {

    private final String uploadDir = "uploads" + File.separator + "images";

    public String saveImageFromUrl(String imageUrl) {
        try {
            String fileName = UUID.randomUUID().toString() + ".png";
            Path directoryPath = Paths.get(System.getProperty("user.dir"), uploadDir);
            
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = directoryPath.resolve(fileName);
            
            try (InputStream in = new URL(imageUrl).openStream();
                 FileOutputStream out = new FileOutputStream(filePath.toFile())) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            return "/uploads/images/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image from URL: " + imageUrl, e);
        }
    }
}
