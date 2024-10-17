package pl.matkan.wholesaler.contactperson.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.matkan.wholesaler.contactperson.ContactPerson;
import pl.matkan.wholesaler.contactperson.ContactPersonDetailedRequest;
import pl.matkan.wholesaler.contactperson.ContactPersonRequest;
import pl.matkan.wholesaler.contactperson.ContactPersonResponse;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactPersonMapper {
    ContactPersonMapper INSTANCE = Mappers.getMapper(ContactPersonMapper.class);

    @Mapping(source = "companyId", target = "company.id")
    @Mapping(source = "ownerId", target = "user.id")
    ContactPerson contactPersonRequestToContactPerson(ContactPersonDetailedRequest contactPersonRequest);


    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    @Mapping(source = "user.id", target = "ownerId")
    @Mapping(source = "user.username", target = "ownerUsername")
    ContactPersonResponse contactPersonToContactPersonResponse(ContactPerson contactPerson);

}
