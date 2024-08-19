package pl.matkan.wholesaler.tradenote;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.matkan.wholesaler.company.Company;
import pl.matkan.wholesaler.user.User;

@Entity
@Table(name = "trade_notes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TradeNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String companyName;

    private Long ownerId;

    private boolean isDeleted = Boolean.FALSE;
//    @ManyToOne
//    @JoinColumn(name = "company_id")
//    @JsonBackReference(value = "tradeNotesCompany")
//    private Company company;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    @JsonBackReference(value = "tradeNotesUser")
//    private User user;


    public TradeNote(String content) {
        this.content = content;
    }
}


