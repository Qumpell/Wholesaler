package pl.matkan.wholesaler.contactperson;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ContactPersonResponse {
    private Long id;
    private String firstname;
    private String surname;
    private String phoneNumber;
    private String mail;
    private String position;
    private Long ownerId;
    private String companyName;
//    private Long companyId;
}
