package pl.matkan.wholesaler.tradenote;

public record TradeNoteRequest(
        String content,
        String companyName,
        Long ownerId
) {
}
