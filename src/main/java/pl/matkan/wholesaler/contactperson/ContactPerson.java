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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonBackReference(value = "contactPersonsCompany")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "contactPersonsUser")
    private User user;

    private boolean isDeleted = Boolean.FALSE;

}
