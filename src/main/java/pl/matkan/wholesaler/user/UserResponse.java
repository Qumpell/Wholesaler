package pl.matkan.wholesaler.user;

import java.time.LocalDate;


public record UserResponse(
        Long id,
        String firstname,
        String surname,
        LocalDate dateOfBirth,
        String username,
        String roleName,
        Long roleId
) {
}
