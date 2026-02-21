package my.blog.repository;

import my.blog.model.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findAll();
    Post getById(long id);
    long save(String title, String text, String tags);
    void update(Post updatedPost);
    void deleteById(long id);
    void addLike(long id);
}
