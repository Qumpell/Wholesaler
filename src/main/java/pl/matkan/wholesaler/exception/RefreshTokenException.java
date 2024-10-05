package pl.matkan.wholesaler.exception;

import java.io.Serial;

public class RefreshTokenException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RefreshTokenException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
