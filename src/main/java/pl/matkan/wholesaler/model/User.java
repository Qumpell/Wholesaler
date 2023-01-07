package pl.matkan.wholesaler.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
//@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id=?")
//@Where(clause = "is_deleted=false")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public User(String name, String surname, String dateOfBirth, String login,Role role) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.login = login;
        this.role = role;
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