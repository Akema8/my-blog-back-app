package my.blog.dto;

import java.util.List;

public class PostRequestDto {
    private String title;
    private String text;
    private List<String> tags;

    public PostRequestDto() {
    }

    public PostRequestDto(String title, String text, List<String> tags) {
        this.title = title;
        this.text = text;
        this.tags = tags;
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
}

