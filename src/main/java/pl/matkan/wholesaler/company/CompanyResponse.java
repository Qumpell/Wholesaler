package pl.matkan.wholesaler.company;




public record CompanyResponse(
        Long id,
        String nip,
        String regon,
        String name,
        String address,
        String city,
        String industryName,
        Long industryId,
        String ownerUsername,
        Long ownerId
) {
}
