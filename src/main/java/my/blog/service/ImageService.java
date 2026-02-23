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
        if (postId <= 0) {
            throw new IllegalArgumentException("Post ID must be positive");
        }
        Path uploadDir = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadDir)) {
            try {
                Files.createDirectories(uploadDir);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create download directory", e);
            }
        }
        return uploadDir.resolve(String.valueOf(postId));
    }

    public void uploadImage(long postId, MultipartFile image) {
        if (postId <= 0) {
            throw new IllegalArgumentException("Post ID must be positive");
        }
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("The uploaded image is empty or null");
        }
        Path filePath = getFilePath(postId);
        try {
            image.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error saving file: " + e.getMessage(), e);
        }
    }

    public byte[] downloadImage(long postId) {
        if (postId <= 0) {
            throw new IllegalArgumentException("Post ID must be positive");
        }
        Path filePath = getFilePath(postId);
        if (!Files.exists(filePath)) {
            throw new RuntimeException("File not found for post ID: " + postId);
        }
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage(), e);
        }
    }

    public void updateImage(long postId, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("The image being updated is empty or null");
        }
        Path filePath = getFilePath(postId);
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            image.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error updating file: " + e.getMessage(), e);
        }
    }
}
