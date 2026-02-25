package my.blog.repository;

import my.blog.model.Comment;
import my.blog.model.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findAll();
    Post getById(long id);
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
