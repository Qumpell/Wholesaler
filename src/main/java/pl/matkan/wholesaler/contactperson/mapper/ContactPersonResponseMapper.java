//package pl.matkan.wholesaler.contactperson.mapper;
//
//import org.mapstruct.InjectionStrategy;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.ReportingPolicy;
//import org.mapstruct.factory.Mappers;
//import pl.matkan.wholesaler.contactperson.ContactPerson;
//import pl.matkan.wholesaler.contactperson.ContactPersonResponse;
//
//@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
//        unmappedSourcePolicy = ReportingPolicy.IGNORE,
//        unmappedTargetPolicy = ReportingPolicy.IGNORE)
//public interface ContactPersonResponseMapper {
//
//    ContactPersonResponseMapper INSTANCE = Mappers.getMapper(ContactPersonResponseMapper.class);
//
//    @Mapping(source = "id",target = "id")
//    @Mapping(source = "firstname",target = "firstname")
//    @Mapping(source = "surname",target = "surname")
//    @Mapping(source = "phoneNumber",target = "phoneNumber")
//    @Mapping(source = "mail",target = "mail")
//    @Mapping(source = "position",target = "position")
//    @Mapping(source = "companyName",target = "companyName")
//    @Mapping(source = "ownerId",target = "ownerId")
//    ContactPersonResponse contactPersonToContactPersonResponse(ContactPerson contactPerson);
//
//    @Mapping(source = "id",target = "id")
//    @Mapping(source = "firstname",target = "firstname")
//    @Mapping(source = "surname",target = "surname")
//    @Mapping(source = "phoneNumber",target = "phoneNumber")
//    @Mapping(source = "mail",target = "mail")
//    @Mapping(source = "position",target = "position")
//    @Mapping(source = "companyName",target = "companyName")
//    @Mapping(source = "ownerId",target = "ownerId")
//    ContactPerson contactPersonResponseToContactPerson(ContactPersonResponse contactPersonResponse);
//}
