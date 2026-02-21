package my.blog.repository;

import my.blog.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class JdbcNativeUserRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query(
                "select id, title, text, tags, likesCount, commentsCount from posts",
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        rs.getString("tags"),
                        rs.getInt("likesCount"),
                        rs.getInt("commentsCount")
                )
        );
    }

    @Override
    public Post getById(long id) {
        String sql = "select id, title, text, tags, likesCount, commentsCount from posts where id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                (rs, rowNum) ->
                        new Post(
                                rs.getLong("id"),
                                rs.getString("title"),
                                rs.getString("text"),
                                rs.getString("tags"),
                                rs.getInt("likesCount"),
                                rs.getInt("commentsCount")
                        )
        );
    }

    @Override
    public long save(String title, String text, String tags) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("posts")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", title);
        parameters.put("text", text);
        parameters.put("tags", tags);
        Number key = insert.executeAndReturnKey(parameters);
        return key.longValue();
    }

    @Override
    public void update(Post post) {
        String sql = "UPDATE posts SET title = ?, text = ?, tags = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                post.getTitle(),
                post.getText(),
                post.getTags(),
                post.getId()
        );
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("delete from posts where id = ?", id);
    }

}