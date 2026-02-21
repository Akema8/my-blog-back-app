package my.blog.repository;

import my.blog.model.Comment;
import my.blog.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcNativePostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll() {
        String sql = "select id, title, text, tags, likesCount, commentsCount from posts";
        return jdbcTemplate.query(
                sql,
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

    @Override
    public void addLike(long id){
        jdbcTemplate.update("update posts set likesCount = likesCount + 1 WHERE id = ?", id);
    }

    @Override
    public List<Comment> findAllComments(long postId){
        String sql = "select id, text, postId from comments where postId = ?";
        return jdbcTemplate.query(
                sql,
                new Object[] { postId },
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getString("text"),
                        rs.getLong("postId")
                )
        );
    }

    @Override
    public Comment getCommentById(long commentId){
        String sql = "select id, text, postId from comments where id = ?";
        return jdbcTemplate.query(
                sql,
                new Object[] { commentId },
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getString("text"),
                        rs.getLong("postId")
                )
        ).getLast();
    }

    @Override
    public long addComment(String text, long postId) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("comments")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("text", text);
        parameters.put("postId", postId);
        Number key = insert.executeAndReturnKey(parameters);
        return key.longValue();
    }

    @Override
    public void updateComment(Comment post) {
        String sql = "UPDATE comments SET text = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                post.getText(),
                post.getId()
        );
    }

    @Override
    public void deleteComment(long commentId){
        jdbcTemplate.update("delete from comments where id = ?", commentId);
    }
}