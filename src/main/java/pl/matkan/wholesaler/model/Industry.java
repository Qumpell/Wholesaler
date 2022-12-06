package pl.matkan.wholesaler.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "industries")
public class Industry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "industry")
    private List<Company> companies;

    public Industry(Long id, String name) {
        this.id = id;
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
}
