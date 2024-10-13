package pl.matkan.wholesaler.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;



@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final Instant errorTime;
    private final String errorDetails;

    public ResourceNotFoundException(String message, String errorDetails) {
        super(message);
        this.errorTime = Instant.now();
        this.errorDetails = errorDetails;
    }


}
