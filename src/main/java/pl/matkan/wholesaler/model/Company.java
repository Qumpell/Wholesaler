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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nip;

    @ManyToOne
    @JoinColumn(name = "industry_id")
    private Industry industry;
    private String address;
    private String city;

    @OneToMany(mappedBy = "company")
    private List<ContactPerson> contactPerson = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
//    @ManyToMany
//    @JoinTable(name = "companies_trade_notes",
//            joinColumns = @JoinColumn(name = "company_id"),
//            inverseJoinColumns = @JoinColumn(name = "trade_note_id"))
//    private Set<TradeNote> tradeNotes = new HashSet<>();

    @OneToMany(mappedBy = "company")
    private List<TradeNote> tradeNotes = new ArrayList<>();

    private boolean isDeleted = Boolean.FALSE;

    public Company() {
    }

    public Company(String name, String nip, String address, String city, Industry industry, User user) {
        this.name = name;
        this.nip = nip;
        this.address = address;
        this.industry = industry;
        this.city = city;
        this.user = user;
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

//    public List<ContactPerson> getContactPerson() {
//        return contactPerson;
//    }
//
//    public void setContactPerson(List<ContactPerson> contactPerson) {
//        this.contactPerson = contactPerson;
//    }
//
//    public List<TradeNote> getTradeNotes() {
//        return tradeNotes;
//    }
//
//    public void setTradeNotes(List<TradeNote> tradeNotes) {
//        this.tradeNotes = tradeNotes;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
