package pl.matkan.wholesaler.company.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.matkan.wholesaler.company.Company;
import pl.matkan.wholesaler.company.CompanyRequest;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyRequestMapper {
    CompanyRequestMapper INSTANCE = Mappers.getMapper(CompanyRequestMapper.class);

    @Mapping(source = "name",target = "name")
    @Mapping(source = "nip",target = "nip")
    @Mapping(source = "address",target = "address")
    @Mapping(source = "city",target = "city")
    @Mapping(source = "industryName",target = "industryName")
    @Mapping(source = "ownerId",target = "ownerId")
//    @Mapping(source = "industry.name",target = "industryName")
//    @Mapping(source = "user.id",target = "ownerId")
    CompanyRequest companyToCompanyRequest(Company company);

    @Mapping(source = "name",target = "name")
    @Mapping(source = "nip",target = "nip")
    @Mapping(source = "address",target = "address")
    @Mapping(source = "city",target = "city")
    @Mapping(source = "industryName",target = "industryName")
    @Mapping(source = "ownerId",target = "ownerId")
    Company companyRequestToCompany(CompanyRequest companyRequest);
}
