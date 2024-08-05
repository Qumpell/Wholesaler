package pl.matkan.wholesaler.tradenote;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TradeNoteDto {
    private Long id;
    private String content;
    private Long ownerId;
    private String companyName;
    private Long companyId;
}
