package pl.matkan.wholesaler.model;

import javax.persistence.*;

@Entity
@Table(name = "contact_persons")
public class ContactPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String mail;
    private String position;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isDeleted;

    public ContactPerson() {
    }

    public ContactPerson(String name, String surname, String phoneNumber, String mail, String position, Company company, User user) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.position = position;
        this.company = company;
        this.user = user;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
