package my.blog.dto;

import java.util.Objects;

public class CommentDto {
    private long id;
    private String text;
    private long postId;

    public CommentDto(){}
    public CommentDto(long id, String text, long postId) {
        this.id = id;
        this.text = text;
        this.postId = postId;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public long getPostId() {
        return postId;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDto)) return false;
        CommentDto that = (CommentDto) o;
        return id == that.id &&
                postId == that.postId &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, postId);
    }
}
