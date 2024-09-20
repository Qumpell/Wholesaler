package pl.matkan.wholesaler.industry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IndustryRequest(

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name
) {
}
