package pl.matkan.wholesaler.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Unique constraint violation: " + ex.getMessage());
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException ex) {

        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("error details", ex.getErrorDetails());
        body.put("error time", String.valueOf(ex.getErrorTime()));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

