public class DuplicateTaskException extends RuntimeException {
    public DuplicateTaskException() {
        super("Error: Task already exists in the list.");
    }
}
