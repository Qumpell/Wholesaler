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

    private Long companyId;

    private Long ownerId;

    private boolean isDeleted = Boolean.FALSE;

}


