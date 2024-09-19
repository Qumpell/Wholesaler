package pl.matkan.wholesaler.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String surname;
    private String email;

    private LocalDate dateOfBirth;

    @Column(unique = true, nullable = false)
    private String username;

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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "role_id")
//    @JsonBackReference(value = "usersRole")
//    private Role role;

    @ManyToMany(fetch = FetchType.EAGER
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
//            ,cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}
//    )
//    @ManyToMany(fetch = FetchType.EAGER, cascade =
//            CascadeType.ALL)
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
    public void addRole(Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
            role.getUsers().add(this);
        }
    }

    public void removeRole(Role role) {
        if(roles.contains(role)) {
            roles.remove(role);
            role.getUsers().remove(this);
        }
    }
}

