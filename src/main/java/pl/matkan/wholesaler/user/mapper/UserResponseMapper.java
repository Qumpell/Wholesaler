package pl.matkan.wholesaler.user.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.matkan.wholesaler.user.User;
import pl.matkan.wholesaler.user.UserResponse;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserResponseMapper {

    UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "firstname",target = "firstname")
    @Mapping(source = "surname",target = "surname")
    @Mapping(source = "login",target = "login")
    @Mapping(source = "dateOfBirth",target = "dateOfBirth")
//    @Mapping(source = "role.name",target = "roleName")
    @Mapping(source = "roleName",target = "roleName")
    UserResponse userToUserResponse(User user);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "firstname",target = "firstname")
    @Mapping(source = "surname",target = "surname")
    @Mapping(source = "login",target = "login")
    @Mapping(source = "dateOfBirth",target = "dateOfBirth")
    @Mapping(source = "roleName",target = "roleName")
    User userResponseToUser(UserResponse userResponse);
}
