package pl.matkan.wholesaler.user;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

public record UserRequest(

        @NotBlank(message = "First name cannot be blank")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String firstname,

        @NotBlank(message = "Surname cannot be blank")
        @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
        String surname,

        @NotBlank(message = "Email cannot be blank")
        @Size(max = 50)
        @Email(message = "Email should be valid")
        String email,

        @NotNull(message = "Date of birth cannot be null")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @NotBlank(message = "Username cannot be blank")
        @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 120,message = "Password must have at least 8 characters")
        String password,

        @NotEmpty(message = "Role IDs cannot be empty")
        Set<Long> roleIds
) {
}
