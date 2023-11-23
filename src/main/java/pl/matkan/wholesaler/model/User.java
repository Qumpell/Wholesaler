package pl.matkan.wholesaler.model;

import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
//@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id=?")
//@SQLDelete(sql = "UPDATE  users SET is_deleted = IF(is_deleted = false, true, false) WHERE id = ?")
//@Where(clause = "is_deleted=false")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String dateOfBirth;
    private String login;
    private String password;
    private boolean isDeleted = Boolean.FALSE;
    @OneToMany(mappedBy = "user")
    private List<Company> companies = new ArrayList<>();;
    @OneToMany(mappedBy = "user")
    private List<ContactPerson> contactPersonList =  new ArrayList<>();;
    @OneToMany(mappedBy = "user")
    private List<TradeNote> tradeNotes =  new ArrayList<>();;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public User() {
    }

    public User(String name, String surname, String dateOfBirth, String login, String password, Role role) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.login = login;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void addCompany(Company company) {
        companies.add(company);
        company.setUser(this);
    }
    public void removeCompany(Company company) {
        companies.remove(company);
        company.setUser(null);
    }
    public void addContactPerson(ContactPerson contactPerson) {
        contactPersonList.add(contactPerson);
        contactPerson.setUser(this);
    }
    public void removeContactPerson(ContactPerson contactPerson) {
        contactPersonList.remove(contactPerson);
        contactPerson.setUser(null);
    }
    public void addTradeNotes(TradeNote tradeNote) {
        tradeNotes.add(tradeNote);
        tradeNote.setUser(this);
    }
    public void removeTradeNote(TradeNote tradeNote) {
        tradeNotes.remove(tradeNote);
        tradeNote.setUser(null);
    }
}

