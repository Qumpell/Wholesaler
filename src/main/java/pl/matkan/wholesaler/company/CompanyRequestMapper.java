package pl.matkan.wholesaler.company;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyRequestMapper {
    CompanyRequestMapper INSTANCE = Mappers.getMapper(CompanyRequestMapper.class);

    @Mapping(source = "name",target = "name")
    @Mapping(source = "nip",target = "nip")
    @Mapping(source = "address",target = "address")
    @Mapping(source = "city",target = "city")
    @Mapping(source = "industry.name",target = "industryName")
    @Mapping(source = "user.id",target = "ownerId")
    CompanyRequest companyToCompanyDto(Company company);

    @Mapping(source = "name",target = "name")
    @Mapping(source = "nip",target = "nip")
    @Mapping(source = "address",target = "address")
    @Mapping(source = "city",target = "city")
    Company companyDtoToCompany(CompanyRequest companyRequest);
}
