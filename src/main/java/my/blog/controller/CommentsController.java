package my.blog.controller;

import my.blog.dto.CommentDto;
import my.blog.dto.CommentRequestDto;
import my.blog.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{id}/comments")
public class CommentsController {

    private final CommentService commentService;

    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(commentService.getComments(id));
    }

    @GetMapping("/{comId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable(name = "comId") Long commentId) {
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentRequestDto newComment){
        return ResponseEntity.ok(commentService.addComment(newComment));
    }

    @PutMapping(value = "/{comId}")
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto updatedComment) {
        return ResponseEntity.ok(commentService.updateComment(updatedComment));
    }

    @DeleteMapping(value = "/{comId}")
    public ResponseEntity<Void> deleteComment(@PathVariable(name = "comId") Long commentId){
        commentService.delete(commentId);
        return  ResponseEntity.ok().build();
    }
}
