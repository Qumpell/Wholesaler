package pl.matkan.wholesaler.contactperson;

import jakarta.validation.constraints.*;

public record ContactPersonRequest(

        @NotBlank(message = "Firstname is required")
        @Size(min = 2, max = 50, message = "Firstname must be between 2 and 50 characters")
        String firstname,

        @NotBlank(message = "Surname is required")
        @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
        String surname,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+(\\d{1,3})[ -]?\\d{1,4}([ -]?\\d{2,4}){2,3}$", message = "Invalid phone number format")
        String phoneNumber,

        @NotBlank(message = "Email is required")
        @Size(max = 50)
        @Email(message = "Invalid email format")
        String mail,

        @NotBlank(message = "Position is required")
        @Size(min = 2, max = 100, message = "Position must be between 2 and 100 characters")
        String position,

        @NotNull(message = "Company ID is required")
        @Positive(message = "Company ID must be a positive number")
        Long companyId
) {
}
