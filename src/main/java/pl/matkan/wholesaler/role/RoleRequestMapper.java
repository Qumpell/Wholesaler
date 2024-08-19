package pl.matkan.wholesaler.role;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleRequestMapper {
    RoleRequestMapper INSTANCE = Mappers.getMapper(RoleRequestMapper.class);

    @Mapping(source = "name",target = "name")
    Role roleRequestToRole(RoleRequest role);

    @Mapping(source = "name",target = "name")
    RoleRequest roleToRoleRequest(Role role);
}
