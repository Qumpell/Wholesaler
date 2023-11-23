package pl.matkan.wholesaler.model;

import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Table(name = "trade_notes")
//@SQLDelete(sql = "UPDATE  trade_notes SET is_deleted = IF(is_deleted = false, true, false) WHERE id = ?")
public class TradeNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private boolean isDeleted = Boolean.FALSE;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public TradeNote() {
    }

    public TradeNote(String content) {
        this.content = content;
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

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


