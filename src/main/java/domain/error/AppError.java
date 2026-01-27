package domain.error;

public class AppError extends RuntimeException {
    public AppError(String message) {
        super(message);
    }
}
