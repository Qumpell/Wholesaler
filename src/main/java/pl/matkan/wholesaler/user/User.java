package pl.matkan.wholesaler.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
import java.util.Collection;
import java.util.HashSet;
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

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstname;

    @NotBlank(message = "Surname cannot be blank")
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    private String surname;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "companiesUser")
    private List<Company> companies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "contactPersonsUser")
    private List<ContactPerson> contactPersonList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "tradeNotesUser")
    private List<TradeNote> tradeNotes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Collection<Role> roles = new HashSet<>();


    private boolean isDeleted = Boolean.FALSE;

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

