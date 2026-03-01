package my.blog.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super("Post with ID " + id + " not found");
    }
}