package pl.matkan.wholesaler.tradenote;

public record TradeNoteRequest(
        String content,
        Long companyId,
        Long ownerId
) {
}
