package pl.matkan.wholesaler.tradenote;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TradeNoteResponse {
    private Long id;
    private String content;
    private String companyName;
    private String ownerName;
    private Long companyId;
    private Long ownerId;
}
