package my.blog.unit.test.service;

import my.blog.dto.CommentDto;
import my.blog.dto.CommentRequestDto;
import my.blog.model.Comment;
import my.blog.repository.PostRepository;
import my.blog.service.CommentService;
import my.blog.unit.test.service.configuration.TestConfig;
import my.blog.mapper.PostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class CommentServiceTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentService commentService;

    @BeforeEach
    void resetMocks() {
        reset(postRepository);
    }


    @Test
    void testGetComments_success() {
        long postId = 1L;
        Comment comment1 = new Comment(1L, "Comment 1", postId);
        Comment comment2 = new Comment(2L, "Comment 2", postId);

        when(postRepository.findAllComments(postId)).thenReturn(List.of(comment1, comment2));
        List<CommentDto> comments = commentService.getComments(postId);

        assertEquals(2, comments.size());
        verify(postRepository).findAllComments(postId);
    }

    @Test
    void testGetComments_invalidPostId() {
        long invalidPostId = -1L;

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            commentService.getComments(invalidPostId);
        });
        assertEquals("postId must be positive", ex.getMessage());
    }

    @Test
    void testGetCommentById_success() {
        long commentId = 1L;
        Comment comment = new Comment(commentId, "Comment text", 1L);
        when(postRepository.getCommentById(commentId)).thenReturn(comment);

        CommentDto result = commentService.getCommentById(commentId);
        assertNotNull(result);
        assertEquals(commentId, result.getId());
        verify(postRepository).getCommentById(commentId);
    }

    @Test
    void testGetCommentById_notFound() {
        long commentId = 999L;
        when(postRepository.getCommentById(commentId)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            commentService.getCommentById(commentId);
        });
        assertEquals("Comment with ID " + commentId + " not found", ex.getMessage());
    }

    @Test
    void testGetCommentById_invalidCommentId() {
        long invalidCommentId = 0L;

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            commentService.getCommentById(invalidCommentId);
        });
        assertEquals("Invalid commentId: " + invalidCommentId, ex.getMessage());
    }

    @Test
    void testAddComment_success() {
        CommentRequestDto requestDto = new CommentRequestDto("New comment", 1L);
        long newCommentId = 100L;
        Comment comment = new Comment(newCommentId, "New comment", 1L);

        when(postRepository.addComment(requestDto.getText(), requestDto.getPostId())).thenReturn(newCommentId);
        when(postRepository.getCommentById(newCommentId)).thenReturn(comment);

        CommentDto result = commentService.addComment(requestDto);

        assertNotNull(result);
        assertEquals(newCommentId, result.getId());
        verify(postRepository).addComment(requestDto.getText(), requestDto.getPostId());
    }

    @Test
    void testAddComment_invalidRequest() {
        CommentRequestDto invalidRequest = new CommentRequestDto("", -1L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            commentService.addComment(invalidRequest);
        });
        assertTrue(ex.getMessage().contains("Comment text cannot be empty") || ex.getMessage().contains("postId must be positive"));
    }

    @Test
    void testUpdateComment_success() {
        CommentDto updatedComment = new CommentDto(1L, "Updated comment", 1L);
        Comment existingComment = new Comment(1L, "Old comment", 1L);
        Comment updatedCommentEntity = new Comment(1L, "Updated comment", 1L);

        when(postRepository.getCommentById(1L)).thenReturn(existingComment);
        when(postRepository.getCommentById(1L)).thenReturn(existingComment);
        when(postRepository.getCommentById(updatedComment.getId())).thenReturn(existingComment);
        doNothing().when(postRepository).updateComment(any());
        when(postRepository.getCommentById(updatedComment.getId())).thenReturn(updatedCommentEntity);

        CommentDto result = commentService.updateComment(updatedComment);
        assertEquals("Updated comment", result.getText());
        verify(postRepository).updateComment(any());
    }

    @Test
    void testUpdateComment_notFound() {
        CommentDto updatedComment = new CommentDto(999L, "Text", 1L);
        when(postRepository.getCommentById(999L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            commentService.updateComment(updatedComment);
        });
        assertEquals("Comment with ID 999 not found", ex.getMessage());
    }

    @Test
    void testUpdateComment_invalidComment() {
        CommentDto invalidComment = null;

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            commentService.updateComment(invalidComment);
        });
        assertTrue(ex.getMessage().contains("Comment cannot be null"));
    }

    @Test
    void testDelete_success() {
        long postId = 1L;
        long commentId = 10L;
        Comment comment = new Comment(commentId, "Text", postId);

        when(postRepository.getCommentById(commentId)).thenReturn(comment);
        doNothing().when(postRepository).deleteComment(commentId);
        doNothing().when(postRepository).decrementComment(postId);

        assertDoesNotThrow(() -> {
            commentService.delete(postId, commentId);
        });
        verify(postRepository).deleteComment(commentId);
        verify(postRepository).decrementComment(postId);
    }

    @Test
    void testDelete_commentNotFound() {
        long postId = 1L;
        long commentId = 999L;
        when(postRepository.getCommentById(commentId)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            commentService.delete(postId, commentId);
        });
        assertEquals("Comment with ID " + commentId + " not found", ex.getMessage());
    }

    @Test
    void testDelete_invalidIds() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> {
            commentService.delete(-1, 1);
        });
        assertEquals("postId must be positive", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> {
            commentService.delete(1, -1);
        });

        assertEquals("Invalid commentId: "+ -1, ex2.getMessage());
    }
}