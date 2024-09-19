package pl.matkan.wholesaler.company;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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

    @Column(name = "nip", nullable = false)
    private String  nip;

    @Column(name = "regon")
    private String regon;

    @Column(name = "name", nullable = false)
    private String name;

    private String city;

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
