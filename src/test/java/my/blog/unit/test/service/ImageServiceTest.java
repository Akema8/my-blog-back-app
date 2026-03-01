package my.blog.unit.test.service;


import my.blog.exception.NotFoundException;
import my.blog.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @Mock
    private MultipartFile multipartFile;

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadImage_success() throws IOException {
        long postId = 1L;

        when(multipartFile.isEmpty()).thenReturn(false);
        doNothing().when(multipartFile).transferTo((Path) any());
        assertDoesNotThrow(() -> imageService.uploadImage(postId, multipartFile));
        verify(multipartFile, times(1)).transferTo((Path) any());
    }

    @Test
    void testUploadImage_nullFile_exception() {
        long postId = 1L;
        assertThrows(IllegalArgumentException.class, () -> imageService.uploadImage(postId, null));
    }

    @Test
    void testUploadImage_emptyFile_exception() {
        long postId = 1L;
        when(multipartFile.isEmpty()).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> imageService.uploadImage(postId, multipartFile));
    }

    @Test
    void testDownloadImage_fileExists_success() throws IOException {
        long postId = 2L;
        Path filePath = Paths.get(ImageService.UPLOAD_DIR, String.valueOf(postId) + ".jpeg");
        Files.createDirectories(filePath.getParent());
        byte[] data = "test data".getBytes();
        Files.write(filePath, data);

        byte[] result = imageService.downloadImage(postId);
        assertArrayEquals(data, result);

        Files.deleteIfExists(filePath);
    }

    @Test
    void testDownloadImage_fileNotFound_exception() {
        long postId = 999L;
        assertThrows(NotFoundException.class, () -> imageService.downloadImage(postId));
    }

    @Test
    void testUpdateImage_fileExists_success() throws IOException {
        long postId = 3L;

        Path filePath = Paths.get(ImageService.UPLOAD_DIR, String.valueOf(postId));
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, "old data".getBytes());

        when(multipartFile.isEmpty()).thenReturn(false);
        doAnswer(invocation -> {
            Path path = invocation.getArgument(0);
            Files.createFile(path);
            return null;
        }).when(multipartFile).transferTo((Path) any());

        assertDoesNotThrow(() -> imageService.updateImage(postId, multipartFile));
        assertTrue(Files.exists(filePath));
        Files.deleteIfExists(filePath);
    }

    @Test
    void testUpdateImage_nullFile_exception() {
        long postId = 4L;
        assertThrows(IllegalArgumentException.class, () -> imageService.updateImage(postId, null));
    }

    @Test
    void testUpdateImage_emptyFile_exception() {
        long postId = 4L;
        when(multipartFile.isEmpty()).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> imageService.updateImage(postId, multipartFile));
    }
}