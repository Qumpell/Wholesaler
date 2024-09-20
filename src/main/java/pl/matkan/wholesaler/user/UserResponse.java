package pl.matkan.wholesaler.user;

import pl.matkan.wholesaler.role.Role;

import java.time.LocalDate;
import java.util.Set;


public record UserResponse(
        Long id,
        String firstname,
        String surname,
        String email,
        LocalDate dateOfBirth,
        String username,
        Set<Role> roles
) {
}
