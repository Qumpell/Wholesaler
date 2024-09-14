package pl.matkan.wholesaler.company;




public record CompanyResponse(
        Long id,
        Integer nip,
        Integer regon,
        String name,
        String address,
        String city,
        String industryName,
        Long industryId,
        String ownerUsername,
        Long ownerId
) {
}
