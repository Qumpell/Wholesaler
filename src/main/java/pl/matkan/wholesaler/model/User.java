package pl.matkan.wholesaler.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private String dateOfBirth;
    private String login;

    @OneToMany(
            mappedBy = "user"
    )
    private List<Company> companies = new ArrayList<>();

    @OneToMany(
            mappedBy = "user"
    )
    private List<ContactPerson> contactPeople = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<TradeNote> tradeNotes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    private boolean isDeleted = Boolean.FALSE;

    public User() {
    }

    public User(String name, String surname, String dateOfBirth, String login) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
//
//    public int getRoleId() {
//        return roleId;
//    }
//
//    public void setRoleId(int roleId) {
//        this.roleId = roleId;
//    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}