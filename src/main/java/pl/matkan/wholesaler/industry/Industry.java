package pl.matkan.wholesaler.industry;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.matkan.wholesaler.company.Company;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "industries")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Industry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "industry")
    @JsonManagedReference(value = "companiesIndustry")
    private List<Company> companies =  new ArrayList<>();


    public void addCompany(Company company) {
        companies.add(company);
        company.setIndustry(this);
    }
    public void removeCompany(Company company) {
        companies.remove(company);
        company.setIndustry(null);
    }

}