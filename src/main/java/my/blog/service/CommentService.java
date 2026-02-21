package my.blog.service;

import my.blog.dto.CommentDto;
import my.blog.dto.CommentRequestDto;
import my.blog.mapper.PostMapper;
import my.blog.repository.PostRepository;
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
        return postRepository.findAllComments(postId).stream()
                .map(postMapper::toCommentDTO)
                .toList();
    }

    public CommentDto getCommentById(long commentId){
        return postMapper.toCommentDTO(postRepository.getCommentById(commentId));
    }

    public CommentDto addComment(CommentRequestDto newComment){
        long newCommentId = postRepository.addComment(newComment.getText(), newComment.getPostId());
        return getCommentById(newCommentId);
    }

    public CommentDto updateComment(CommentDto updatedComment){
        postRepository.updateComment(postMapper.toComment(updatedComment));
        return getCommentById(updatedComment.getId());
    }

    public void delete(long commentId){
        postRepository.deleteComment(commentId);
    }
}
