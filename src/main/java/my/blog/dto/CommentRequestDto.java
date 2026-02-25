package my.blog.dto;

public class CommentRequestDto {
    private String text;
    private long postId;

    public CommentRequestDto(){
    }

    public CommentRequestDto(String text, long postId){
        this.text = text;
        this.postId = postId;
    }



    public String getText() {
        return text;
    }

    public long getPostId() {
        return postId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }
}
