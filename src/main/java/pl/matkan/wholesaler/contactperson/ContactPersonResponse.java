package pl.matkan.wholesaler.contactperson;

public record ContactPersonResponse(    
        Long id,
        String firstname,
        String surname,
        String phoneNumber,
        String mail,
        String position,
        String companyName,
        Long companyId,
        String ownerUsername,
        Long ownerId
){
}
