package my.blog.integration.test.repository;

import my.blog.configuration.DataSourceConfiguration;
import my.blog.model.Post;
import my.blog.repository.JdbcNativePostRepository;
import my.blog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, JdbcNativePostRepository.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class PostRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("""
                    INSERT INTO posts (title, text, tags, likesCount, commentsCount) VALUES
                                                           ( 'Название поста 1', 'Текст поста1...', 'tag_1,tag_2', 5, 3),
                                                           ( 'Название поста 2', 'Текст поста2...', '', 1, 1);
                """);
        jdbcTemplate.execute("""
                    INSERT INTO comments (text, postId) VALUES
                                                               ( 'комментарий 1', 1),
                                                               ( 'комментарий 2', 1),
                                                               ( 'комментарий 3', 1),
                                                               ( 'комментарий 1', 2);
                """);
    }

    @Test
    void save_shouldAddPostToDatabase_success() {
        postRepository.save("Название поста 3","Текст поста3...", "");

        List<Post> all = postRepository.findAll();
        Post saved = all.stream().filter(u -> u.getTitle().equals("Название поста 3")).findFirst().orElse(null);

        assertNotNull(saved);
        assertEquals("Текст поста3...", saved.getText());
        assertEquals("", saved.getTags());
    }

    @Test
    void findAll_shouldReturnAllPosts_success() {
        List<Post> posts = postRepository.findAll();

        assertNotNull(posts);
        assertEquals(2, posts.size());
        Post first = posts.getFirst();
        Post second = posts.get(1);
        assertEquals("Название поста 1", first .getTitle());
        assertEquals("Текст поста1...", first.getText());
        assertEquals("Название поста 2", second.getTitle());
        assertEquals("Текст поста2...", second.getText());
    }

    @Test
    void deleteById_shouldRemovePostFromDatabase_success() {
        Post post = postRepository.getById(1L);
        assertNotNull(post);
        postRepository.deleteById(1L);

        List<Post> posts = postRepository.findAll();
        assertEquals(1, posts.size());
    }

}
