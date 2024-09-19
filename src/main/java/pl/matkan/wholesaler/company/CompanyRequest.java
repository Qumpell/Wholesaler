package pl.matkan.wholesaler.company;

public record CompanyRequest(
        String nip,
        String regon,
        String name,
        String address,
        String city,
        Long industryId,
        Long ownerId) {
}
