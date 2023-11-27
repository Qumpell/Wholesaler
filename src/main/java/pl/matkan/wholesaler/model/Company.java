package pl.matkan.wholesaler.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
//@SQLDelete(sql = "UPDATE  companies SET is_deleted = IF(is_deleted = false, true, false) WHERE id = ?")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nip;
    private String address;
    private String city;
    private boolean isDeleted = Boolean.FALSE;
    @ManyToOne
    @JoinColumn(name = "industry_id")
    private Industry industry;
//    @OneToMany(mappedBy = "company", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ContactPerson> contactPersonList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
//    @OneToMany(mappedBy = "company",  cascade = CascadeType.PERSIST)
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TradeNote> tradeNotes =  new ArrayList<>();


    public Company() {
    }

    public Company(String name, String nip, String address, String city) {
        this.name = name;
        this.nip = nip;
        this.address = address;
        this.city = city;
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
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<ContactPerson> getContactPersonList() {
        return contactPersonList;
    }

    public void setContactPersonList(List<ContactPerson> contactPersonList) {
        this.contactPersonList = contactPersonList;
    }

    public List<TradeNote> getTradeNotes() {
        return tradeNotes;
    }

    public void setTradeNotes(List<TradeNote> tradeNotes) {
        this.tradeNotes = tradeNotes;
    }

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
