package pl.matkan.wholesaler.contactperson;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.matkan.wholesaler.company.Company;
import pl.matkan.wholesaler.user.User;



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
  
    private String firstname;
    private String surname;
    private String phoneNumber;
    private String mail;
    private String position;
    private String companyName;
    private Long ownerId;

    private boolean isDeleted = Boolean.FALSE;

//    @ManyToOne
//    @JoinColumn(name = "company_id")
//    @JsonBackReference(value = "contactPersonsCompany")
//    private Company company;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    @JsonBackReference(value = "contactPersonsUser")
//    private User user;

    public ContactPerson(String name, String surname, String phoneNumber, String mail, String position) {
        this.firstname = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.position = position;
    }

}
