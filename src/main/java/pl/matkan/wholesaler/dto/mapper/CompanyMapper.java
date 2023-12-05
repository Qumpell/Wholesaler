package pl.matkan.wholesaler.dto.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.matkan.wholesaler.dto.CompanyDto;
import pl.matkan.wholesaler.model.Company;
import pl.matkan.wholesaler.model.User;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "name",target = "name")
    @Mapping(source = "nip",target = "nip")
    @Mapping(source = "address",target = "address")
    @Mapping(source = "city",target = "city")
    @Mapping(source = "industry.name",target = "industryName")
    @Mapping(source = "user.id",target = "ownerId")
    CompanyDto companyToCompanyDto(Company company);
}
