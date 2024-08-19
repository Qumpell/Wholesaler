package pl.matkan.wholesaler.user;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class UserResponse {
    private Long id;
    private String firstname;
    private String surname;
    private LocalDate dateOfBirth;
    private String login;
    private String roleName;

}
