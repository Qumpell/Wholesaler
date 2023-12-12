package pl.matkan.wholesaler.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "industries")
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
    public Industry() {

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

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
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