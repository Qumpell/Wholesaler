package pl.matkan.wholesaler.company;

public record CompanyRequest(
        String name,
        String nip,
        String address,
        String city,
        String industryName,
        Long ownerId) {
}
