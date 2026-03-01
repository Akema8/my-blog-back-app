package my.blog.repository;

import my.blog.model.Comment;
import my.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    List<Post> findAll();
    Optional<Post> getById(long id);
    long save(String title, String text, String tags);
    void update(Post updatedPost);
    void deleteById(long id);
    void addLike(long id);
    List<Comment> findAllComments(long postId);
    Comment getCommentById(long commentId);
    long addComment(String text, long postId);
    void updateComment(Comment post);
    void deleteComment(long commentId);
    void incrementComment(long postId);
    void decrementComment(long postId);
}
