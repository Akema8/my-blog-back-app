package my.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {
    public static final String UPLOAD_DIR = "uploads/";

    private Path getFilePath(long postId) {
        Path uploadDir = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadDir)) {
            try {
                Files.createDirectories(uploadDir);
            } catch (IOException e) {
                throw new RuntimeException("Не удалось создать директорию для загрузки", e);
            }
        }
        return uploadDir.resolve(String.valueOf(postId));
    }

    public void uploadImage(long postId, MultipartFile image) {
        Path filePath = getFilePath(postId);
        try {
            image.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public byte[] downloadImage(long postId) {
        Path filePath = getFilePath(postId);
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void updateImage(long postId, MultipartFile image) {
        Path filePath = getFilePath(postId);
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            image.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
