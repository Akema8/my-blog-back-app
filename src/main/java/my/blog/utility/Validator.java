package my.blog.utility;

import my.blog.dto.CommentDto;
import my.blog.dto.CommentRequestDto;
import my.blog.dto.PostRequestDto;
import my.blog.dto.PostUpdateRequestDto;

public class Validator {

    public static void validatePostId(long postId) {
        if (postId <= 0) {
            throw new IllegalArgumentException("postId must be positive");
        }
    }

    public static void validateCommentId(long commentId) {
        if (commentId <= 0) {
            throw new IllegalArgumentException("Invalid commentId: " + commentId);
        }
    }

    public static void validateCommentRequest(CommentRequestDto comment) {
        if (comment == null || comment.getText() == null || comment.getText().isBlank()) {
            throw new IllegalArgumentException("Comment text cannot be empty");
        }
        validatePostId(comment.getPostId());
    }

    public static void validateCommentDto(CommentDto comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        validateCommentId(comment.getId());
    }

    public static void validatePostRequest(PostRequestDto postRequest) {
        if (postRequest == null) {
            throw new IllegalArgumentException("PostRequestDto must not be null");
        }
        if (postRequest.getTitle() == null || postRequest.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Post title must not be null or empty");
        }
        if (postRequest.getText() == null || postRequest.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Post text must not be null or empty");
        }
    }

    public static void validatePostUpdate(PostUpdateRequestDto postUpdate) {
        validatePostId(postUpdate.getId());
        validatePostRequest(new PostRequestDto(postUpdate.getTitle(), postUpdate.getText()));
    }

}