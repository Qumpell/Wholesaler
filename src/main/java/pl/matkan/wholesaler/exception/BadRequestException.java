package pl.matkan.wholesaler.exception;

import lombok.Getter;

import java.time.Instant;


@Getter
public class BadRequestException extends RuntimeException {

    private final Instant errorTime;
    private final String errorDetails;


    public BadRequestException(String message, String errorDetails) {
        super(message);
        this.errorTime = Instant.now();
        this.errorDetails = errorDetails;
    }
}
