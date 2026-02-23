package my.blog.dto;

import java.util.List;
import java.util.Objects;

public class PostDto {
    private long id;
    private String title;
    private String text;
    private List<String> tags;
    private int likesCount;
    private int commentsCount;

    public PostDto(){}
    public PostDto(long id, String title, String text, List<String> tags, int likesCount, int commentsCount) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.tags = tags;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
    }
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostDto)) return false;
        PostDto postDto = (PostDto) o;
        return id == postDto.id &&
                likesCount == postDto.likesCount &&
                commentsCount == postDto.commentsCount &&
                Objects.equals(title, postDto.title) &&
                Objects.equals(text, postDto.text) &&
                Objects.equals(tags, postDto.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, text, tags, likesCount, commentsCount);
    }
}