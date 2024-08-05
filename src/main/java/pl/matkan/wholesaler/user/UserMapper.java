package pl.matkan.wholesaler.user;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "name",target = "firstname")
    @Mapping(source = "surname",target = "surname")
    @Mapping(source = "login",target = "login")
    @Mapping(source = "dateOfBirth",target = "dateOfBirth")
    @Mapping(source = "role.name",target = "roleName")
    UserDto userToUserDto(User user);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "firstname",target = "name")
    @Mapping(source = "surname",target = "surname")
    @Mapping(source = "login",target = "login")
    @Mapping(source = "dateOfBirth",target = "dateOfBirth")
    User userDtoToUser(UserDto userDto);
}
