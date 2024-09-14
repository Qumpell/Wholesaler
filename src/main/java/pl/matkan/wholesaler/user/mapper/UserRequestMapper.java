//package pl.matkan.wholesaler.user.mapper;
//
//import org.mapstruct.InjectionStrategy;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.ReportingPolicy;
//import org.mapstruct.factory.Mappers;
//import pl.matkan.wholesaler.user.User;
//import pl.matkan.wholesaler.user.UserRequest;
//
//@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
//        unmappedSourcePolicy = ReportingPolicy.IGNORE,
//        unmappedTargetPolicy = ReportingPolicy.IGNORE)
//public interface UserRequestMapper {
//
//    UserRequestMapper INSTANCE = Mappers.getMapper(UserRequestMapper.class);
//
//
//    @Mapping(source = "firstname",target = "firstname")
//    @Mapping(source = "surname",target = "surname")
//    @Mapping(source = "login",target = "login")
//    @Mapping(source = "password",target = "password")
//    @Mapping(source = "dateOfBirth",target = "dateOfBirth")
//    @Mapping(source = "roleName",target = "roleName")
//    UserRequest userToUserRequest(User user);
//
//
//    @Mapping(source = "firstname",target = "firstname")
//    @Mapping(source = "surname",target = "surname")
//    @Mapping(source = "login",target = "login")
//    @Mapping(source = "password",target = "password")
//    @Mapping(source = "dateOfBirth",target = "dateOfBirth")
//    @Mapping(source = "roleName",target = "roleName")
//    User userRequestToUser(UserRequest userResponse);
//}
