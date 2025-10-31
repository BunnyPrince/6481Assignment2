public class InvalidTaskException extends RuntimeException {
    public InvalidTaskException() {
        super("Error: Invalid or missing task information.");
    }
}
