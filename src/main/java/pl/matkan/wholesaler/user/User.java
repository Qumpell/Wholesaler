package pl.matkan.wholesaler.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.matkan.wholesaler.company.Company;
import pl.matkan.wholesaler.contactperson.ContactPerson;
import pl.matkan.wholesaler.role.Role;
import pl.matkan.wholesaler.tradenote.TradeNote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String login;
    private String password;
    private boolean isDeleted = Boolean.FALSE;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "companiesUser")
    private List<Company> companies = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "contactPersonsUser")
    private List<ContactPerson> contactPersonList =  new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "tradeNotesUser")
    private List<TradeNote> tradeNotes =  new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonBackReference(value = "usersRole")
    private Role role;


    public User(String name, String surname, LocalDate dateOfBirth, String login, String password, Role role) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.login = login;
        this.password = password;
        this.role = role;
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

