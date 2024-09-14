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
@Table(name = "companies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nip", unique = true, nullable = false)
    private Integer nip;

    @Column(name = "regon", unique = true, nullable = false)
    private Integer regon;

    @Column(name = "name", unique = true, nullable = false)
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
