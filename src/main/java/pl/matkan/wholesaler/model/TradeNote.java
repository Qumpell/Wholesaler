package pl.matkan.wholesaler.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="trade_notes" )
public class TradeNote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    private boolean isDeleted = Boolean.FALSE;

    @ManyToMany
    private Set<Company> companies = new HashSet<>();
    //private Long companyId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public TradeNote() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
