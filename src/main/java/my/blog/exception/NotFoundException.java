package my.blog.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entity, Long id) {
        super(entity +" with ID " + id + " not found");
    }
}