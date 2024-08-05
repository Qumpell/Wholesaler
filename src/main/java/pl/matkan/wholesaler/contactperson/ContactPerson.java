package pl.matkan.wholesaler.contactperson;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.matkan.wholesaler.company.Company;
import pl.matkan.wholesaler.user.User;

import javax.persistence.*;

@Entity
@Table(name = "contact_persons")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    private String name;
    private String surname;
    private String phoneNumber;
    private String mail;
    private String position;
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonBackReference(value = "contactPersonsCompany")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "contactPersonsUser")
    private User user;

    public ContactPerson(String name, String surname, String phoneNumber, String mail, String position) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.position = position;
    }

}
