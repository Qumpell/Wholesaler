package pl.matkan.wholesaler.contactperson;

public record ContactPersonRequest(
        String firstname,
        String surname,
        String phoneNumber,
        String mail,
        String position,
        Long ownerId,
        String companyName) {
//        Long companyId) {
}
