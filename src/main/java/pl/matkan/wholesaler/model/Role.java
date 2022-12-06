package pl.matkan.wholesaler.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String industryName;

    @OneToMany(mappedBy = "role")
    private List<User> users;
    public Role() {
    }

    public Role(Long id, String industryName) {
        this.id = id;
        this.industryName = industryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }
}
