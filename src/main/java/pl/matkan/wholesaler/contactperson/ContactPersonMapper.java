package pl.matkan.wholesaler.contactperson;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactPersonMapper {
    ContactPersonMapper INSTANCE = Mappers.getMapper(ContactPersonMapper.class);
    @Mapping(source = "id",target = "id")
    @Mapping(source = "name",target = "firstname")
    @Mapping(source = "surname",target = "surname")
    @Mapping(source = "phoneNumber",target = "phoneNumber")
    @Mapping(source = "mail",target = "mail")
    @Mapping(source = "position",target = "position")
    @Mapping(source = "user.id",target = "ownerId")
    @Mapping(source = "company.name",target = "companyName")
    @Mapping(source = "company.id",target = "companyId")
    ContactPersonDto contactPersonToContactPersonDto(ContactPerson contactPerson);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "firstname",target = "name")
    @Mapping(source = "surname",target = "surname")
    @Mapping(source = "phoneNumber",target = "phoneNumber")
    @Mapping(source = "mail",target = "mail")
    @Mapping(source = "position",target = "position")
    ContactPerson contactPersonDtoToContactPerson(ContactPersonDto contactPersonDto);
}
