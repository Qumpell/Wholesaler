package pl.matkan.wholesaler.dto;


import java.time.LocalDate;

public class UserDto {
    private String firstname;
    private String surname;
    private LocalDate dateOfBirth;
    private String login;
    private String roleName;
    private Long id;

    public UserDto(String firstname, String surname, LocalDate dateOfBirth, String login, String roleName, Long id) {
        this.firstname = firstname;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.login = login;
        this.roleName = roleName;
        this.id = id;
    }


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
