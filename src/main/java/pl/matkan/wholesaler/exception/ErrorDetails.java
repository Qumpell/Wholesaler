package pl.matkan.wholesaler.exception;

import java.util.Date;

public record ErrorDetails(
        Date date,
        String message,
        String description
) {
}
