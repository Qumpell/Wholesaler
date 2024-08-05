package pl.matkan.wholesaler.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;



@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class EntityNotFoundException extends RuntimeException {
    private final Instant errorTime;
    private final String errorDetails;

    public EntityNotFoundException(String message, String errorDetails) {
        super(message);
        this.errorTime = Instant.now();
        this.errorDetails = errorDetails;
    }


}
