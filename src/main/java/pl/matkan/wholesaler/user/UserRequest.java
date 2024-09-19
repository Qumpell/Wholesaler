package pl.matkan.wholesaler.user;

import java.time.LocalDate;
import java.util.Set;

public record UserRequest(
            String firstname,
            String surname,
            String email,
            LocalDate dateOfBirth,
            String username,
            String password,
            Set<Long> roleIds
) {
}
