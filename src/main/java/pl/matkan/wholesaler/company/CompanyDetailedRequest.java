package pl.matkan.wholesaler.company;

import jakarta.validation.constraints.*;

public record CompanyDetailedRequest(
        @Pattern(regexp = "^[A-Za-z]{2,4}(?=.{2,12}$)[-_\\s0-9]*(?:[a-zA-Z][-_\\s0-9]*){0,2}$")
        String nip,

        @Pattern(regexp = "\\d{9}|\\d{14}|", message = "REGON must consist of 9 or 14 digits or can be empty for companies outside Poland")
        String regon,

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @NotBlank(message = "Address cannot be blank")
        @Size(max = 255, message = "Address must be at most 255 characters long")
        String address,

        @NotBlank(message = "City cannot be blank")
        @Size(max = 50, message = "City name must be at most 100 characters long")
        String city,

        @NotNull(message = "Industry ID is required")
        @Positive
        Long industryId,

        @NotNull(message = "Owner ID is required")
        @Positive
        Long ownerId
) {
}
