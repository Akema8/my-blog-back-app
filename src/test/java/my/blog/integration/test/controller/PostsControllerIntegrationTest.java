package my.blog.integration.test.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class PostsControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("""
    INSERT INTO posts (id, title, text, tags, likesCount, commentsCount) VALUES
        (2, 'Название поста 2', 'Текст поста1...', 'tag_1,tag_2', 5, 3),
        (3, 'Название поста 3', 'Текст поста2...', '', 1, 1)
""");
        jdbcTemplate.execute("""
    INSERT INTO comments (text, postId) VALUES
        ('комментарий 1', 1),
        ('комментарий 2', 1),
        ('комментарий 3', 1),
        ('комментарий 1', 2)
""");
    }

    @Test
    public void testFilterPosts_success() throws Exception {
        mockMvc.perform(get("/api/posts?search=&pageNumber=1&pageSize=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts[0].title").value("Название поста 2"))
                .andExpect(jsonPath("$.posts[1].title").value("Название поста 3"));

        mockMvc.perform(get("/api/posts")
                        .param("search", "2")
                        .param("pageNumber", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.posts.length()").value(1))
                .andExpect(jsonPath("$.posts[0].title").value("Название поста 2"));
    }

    @Test
    public void testCreateAndGetPost_success() throws Exception {
        String json = """
                {
                    "title":"Test Post",
                    "text":"Test Text Text",
                    "tags": []
                }
                """;

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"))
                .andExpect(jsonPath("$.text").value("Test Text Text"))
                .andExpect(jsonPath("$.likesCount").value(0))
                .andExpect(jsonPath("$.commentsCount").value(0));

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Post"));
    }

    @Test
    public void testDeletePost_scuccess() throws Exception {
        mockMvc.perform(get("/api/posts/2"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/posts/2"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/posts/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePost_success() throws Exception {
        // Данные для обновления поста с id=1
        String json = """
            {
                "id": 2,
                "title": "Обновленный заголовок",
                "text": "Обновленный текст поста",
                "tags": ["новый", "тег"]
            }
            """;

        mockMvc.perform(put("/api/posts/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Обновленный заголовок"))
                .andExpect(jsonPath("$.text").value("Обновленный текст поста"))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags[0]").value("новый"))
                .andExpect(jsonPath("$.tags[1]").value("тег"));

        mockMvc.perform(get("/api/posts/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Обновленный заголовок"))
                .andExpect(jsonPath("$.text").value("Обновленный текст поста"))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags[0]").value("новый"))
                .andExpect(jsonPath("$.tags[1]").value("тег"));
    }
}