package pl.matkan.wholesaler.user;

import java.time.LocalDate;

public record UserRequest(
            String firstname,
            String surname,
            LocalDate dateOfBirth,
            String login,
            String password,
            String roleName) {
}
