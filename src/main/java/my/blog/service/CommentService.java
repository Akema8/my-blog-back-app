package my.blog.service;

import my.blog.dto.CommentDto;
import my.blog.dto.CommentRequestDto;
import my.blog.mapper.PostMapper;
import my.blog.model.Comment;
import my.blog.repository.PostRepository;
import my.blog.utility.Validator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public CommentService(PostRepository _postRepository, PostMapper _postMapper) {
        this.postRepository = _postRepository;
        this.postMapper = _postMapper;
    }

    public List<CommentDto> getComments(long postId){
        Validator.validatePostId(postId);
        return postRepository.findAllComments(postId).stream()
                .map(postMapper::toCommentDTO)
                .toList();
    }

    public CommentDto getCommentById(long commentId){
        Validator.validateCommentId(commentId);
        Comment comment = postRepository.getCommentById(commentId);
        if (comment == null){
            throw new RuntimeException("Comment with ID " + commentId + " not found");
        }
        return postMapper.toCommentDTO(comment);
    }

    public CommentDto addComment(CommentRequestDto newComment){
        Validator.validateCommentRequest(newComment);
        long newCommentId = postRepository.addComment(newComment.getText(), newComment.getPostId());
        postRepository.incrementComment(newComment.getPostId());
        return getCommentById(newCommentId);
    }

    public CommentDto updateComment(CommentDto updatedComment){
        Validator.validateCommentDto(updatedComment);
        Comment comment = postRepository.getCommentById(updatedComment.getId());
        if (comment == null){
            throw new RuntimeException("Comment with ID " + updatedComment.getId() + " not found");
        }
        postRepository.updateComment(postMapper.toComment(updatedComment));
        return getCommentById(updatedComment.getId());
    }

    public void delete(long postId, long commentId){
        Validator.validatePostId(postId);
        Validator.validateCommentId(commentId);
        Comment comment = postRepository.getCommentById(commentId);
        if (comment == null){
            throw new RuntimeException("Comment with ID " + commentId+ " not found");
        }
        postRepository.deleteComment(commentId);
        postRepository.decrementComment(postId);
    }

}
