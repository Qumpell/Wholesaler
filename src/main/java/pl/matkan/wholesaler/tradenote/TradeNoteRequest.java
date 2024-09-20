package pl.matkan.wholesaler.tradenote;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TradeNoteRequest(

        @NotBlank(message = "Content cannot be blank")
        @Size(min = 10, max = 500, message = "Content must be between 10 and 500 characters")
        String content,

        @NotNull(message = "Company ID cannot be null")
        Long companyId,

        @NotNull(message = "Owner ID cannot be null")
        Long ownerId
) {
}
