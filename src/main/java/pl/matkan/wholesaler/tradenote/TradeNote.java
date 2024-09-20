package pl.matkan.wholesaler.tradenote;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Content cannot be blank")
    @Size(min = 10, max = 500, message = "Content must be between 10 and 500 characters")
    private String content;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonBackReference(value = "tradeNotesCompany")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "tradeNotesUser")
    private User user;

    private boolean isDeleted = Boolean.FALSE;

}


