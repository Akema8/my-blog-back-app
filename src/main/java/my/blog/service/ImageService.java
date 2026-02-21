package my.blog.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {
    public static final String UPLOAD_DIR = "uploads/";

    public void uploadImage(long postId, MultipartFile image) {
        try {
            Path uploadDir = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(String.valueOf(postId));//(image.getOriginalFilename());
            image.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public byte[] downloadImage(long postId) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(String.valueOf(postId)).normalize();
            return Files.readAllBytes(filePath);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
