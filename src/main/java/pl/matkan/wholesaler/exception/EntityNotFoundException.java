package pl.matkan.wholesaler.exception;


import java.time.LocalDateTime;



public class EntityNotFoundException extends RuntimeException {
    private final LocalDateTime errorTime;
    private final String errorDetails;

    public EntityNotFoundException(String message, String errorDetails) {
        super(message);
        this.errorTime = LocalDateTime.now();
        this.errorDetails = errorDetails;
    }

    public LocalDateTime getErrorTime() {
        return errorTime;
    }

    public String getErrorDetails() {
        return errorDetails;
    }
}
