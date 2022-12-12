package pl.matkan.wholesaler.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String nip;

    @ManyToOne
    @JoinColumn(name="industry_id")
    private Industry industry;
    //private Long industryId;
    private String address;

    @OneToMany(mappedBy="company")
    private List<ContactPerson> contactPerson = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany
    @JoinTable(name = "companies_trade_notes",
    joinColumns = @JoinColumn(name="company_id"),
    inverseJoinColumns = @JoinColumn(name = "trade_note_id"))
    private Set<TradeNote> tradeNotes = new HashSet<>();

    private boolean isDeleted = Boolean.FALSE;

    public Company() {}

    public Company(String name, String nip, String address) {
        this.name = name;
        this.nip = nip;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ContactPerson> getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(List<ContactPerson> contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Set<TradeNote> getTradeNotes() {
        return tradeNotes;
    }

    public void setTradeNotes(Set<TradeNote> tradeNotes) {
        this.tradeNotes = tradeNotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
