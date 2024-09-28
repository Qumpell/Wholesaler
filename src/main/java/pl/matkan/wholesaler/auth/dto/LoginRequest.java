package pl.matkan.wholesaler.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "Username cannot be blank")
        @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 120, message = "Password must have at least 8 characters")
        String password
) {
}
