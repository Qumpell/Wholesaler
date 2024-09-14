package pl.matkan.wholesaler.company;

public record CompanyRequest(
        Integer nip,
        Integer regon,
        String name,
        String address,
        String city,
        Long industryId,
        Long ownerId) {
}
