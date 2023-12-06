package pl.matkan.wholesaler.dto.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.matkan.wholesaler.dto.ContactPersonDto;
import pl.matkan.wholesaler.model.ContactPerson;

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
    ContactPersonDto contactPersonToContactPersonDto(ContactPerson contactPerson);
}
