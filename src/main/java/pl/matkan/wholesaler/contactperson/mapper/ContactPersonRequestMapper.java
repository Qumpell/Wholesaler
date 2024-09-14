//package pl.matkan.wholesaler.contactperson.mapper;
//
//import org.mapstruct.InjectionStrategy;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.ReportingPolicy;
//import org.mapstruct.factory.Mappers;
//import pl.matkan.wholesaler.contactperson.ContactPerson;
//import pl.matkan.wholesaler.contactperson.ContactPersonRequest;
//
//@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
//        unmappedSourcePolicy = ReportingPolicy.IGNORE,
//        unmappedTargetPolicy = ReportingPolicy.IGNORE)
//public interface ContactPersonRequestMapper {
//
//    ContactPersonRequestMapper INSTANCE = Mappers.getMapper(ContactPersonRequestMapper.class);
//
//    @Mapping(source = "firstname",target = "firstname")
//    @Mapping(source = "surname",target = "surname")
//    @Mapping(source = "phoneNumber",target = "phoneNumber")
//    @Mapping(source = "mail",target = "mail")
//    @Mapping(source = "position",target = "position")
//    @Mapping(source = "companyName",target = "companyName")
////    @Mapping(source = "companyId",target = "companyId")
//    @Mapping(source = "ownerId",target = "ownerId")
//    ContactPersonRequest contactPersonToContactPersonRequest(ContactPerson contactPerson);
//
//    @Mapping(source = "firstname",target = "firstname")
//    @Mapping(source = "surname",target = "surname")
//    @Mapping(source = "phoneNumber",target = "phoneNumber")
//    @Mapping(source = "mail",target = "mail")
//    @Mapping(source = "position",target = "position")
//    @Mapping(source = "companyName",target = "companyName")
////    @Mapping(source = "companyId",target = "companyId")
//    @Mapping(source = "ownerId",target = "ownerId")
//    ContactPerson contactPersonRequestToContactPerson(ContactPersonRequest contactPersonRequest);
//}
