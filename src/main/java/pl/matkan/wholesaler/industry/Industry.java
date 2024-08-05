package pl.matkan.wholesaler.industry;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.matkan.wholesaler.company.Company;

import javax.persistence.*;
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

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "industry")
    @JsonManagedReference(value = "companiesIndustry")
    private List<Company> companies =  new ArrayList<>();

    public Industry(String name) {
        this.name = name;
    }

    public void addCompany(Company company) {
        companies.add(company);
        company.setIndustry(this);
    }
    public void removeCompany(Company company) {
        companies.remove(company);
        company.setIndustry(null);
    }

}