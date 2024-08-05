package pl.matkan.wholesaler.company;

import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public class CompanyDto {
    private Long id;
    private String name;
    private String nip;
    private String address;
    private String city;
    private String industryName;
    private Long ownerId;

}
