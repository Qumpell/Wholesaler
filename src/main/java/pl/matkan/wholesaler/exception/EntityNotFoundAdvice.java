package pl.matkan.wholesaler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class EntityNotFoundAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String entityNotFoundHandler(EntityNotFoundException ex) {
    public  ResponseEntity<Map<String, Object>> entityNotFoundHandler(EntityNotFoundException ex) {
//        return ex.getMessage();
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("errorDetails", ex.getErrorDetails());
        errorResponse.put("errorTime", ex.getErrorTime());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
