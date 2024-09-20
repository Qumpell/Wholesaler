package pl.matkan.wholesaler.company;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.matkan.wholesaler.contactperson.ContactPerson;
import pl.matkan.wholesaler.industry.Industry;
import pl.matkan.wholesaler.tradenote.TradeNote;
import pl.matkan.wholesaler.user.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "companies",
uniqueConstraints = {
        @UniqueConstraint(name = "companies.unique_nip_idx", columnNames = "nip"),
        @UniqueConstraint(name = "companies.unique_regon_idx", columnNames = "regon"),
        @UniqueConstraint(name = "companies.unique_name_idx", columnNames = "name")
})
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[A-Za-z]{2,4}(?=.{2,12}$)[-_\\s0-9]*(?:[a-zA-Z][-_\\s0-9]*){0,2}$")
    @Column(name = "nip", nullable = false)
    private String  nip;

    @Pattern(regexp = "\\d{9}|\\d{14}|", message = "REGON must consist of 9 or 14 digits or can be empty for companies outside Poland")
    @Column(name = "regon")
    private String regon;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 50, message = "City name must be at most 100 characters long")
    private String city;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 255, message = "Address must be at most 255 characters long")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_id")
    @JsonBackReference(value = "companiesIndustry")
    private Industry industry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "companiesUser")
    private User user;

    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "contactPersonsCompany")
    private List<ContactPerson> contactPersonList = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "tradeNotesCompany")
    private List<TradeNote> tradeNotes =  new ArrayList<>();

    private boolean isDeleted = Boolean.FALSE;


    public void addContactPerson(ContactPerson contactPerson) {
        contactPersonList.add(contactPerson);
        contactPerson.setCompany(this);
    }

    public void removeContactPerson(ContactPerson contactPerson) {
        contactPersonList.remove(contactPerson);
        contactPerson.setCompany(null);
    }

    public void addTradeNote(TradeNote tradeNote) {
        tradeNotes.add(tradeNote);
        tradeNote.setCompany(this);
    }

    public void removeTradeNote(TradeNote tradeNote) {
        tradeNotes.remove(tradeNote);
        tradeNote.setCompany(null);
    }
}
