package pl.matkan.wholesaler.tradenote;



public record TradeNoteResponse(
        Long id,
        String content,
        String companyName,
        Long companyId,
        String ownerUsername,
        Long ownerId
) {
}
