package pl.matkan.wholesaler.company.mapper;


import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.matkan.wholesaler.company.Company;
import pl.matkan.wholesaler.company.CompanyRequest;
import pl.matkan.wholesaler.company.CompanyResponse;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    @Mapping(source = "industryId", target = "industry.id")
    @Mapping(source = "ownerId", target = "user.id")
    Company companyRequestToCompany(CompanyRequest dto);

    @Mapping(source = "industry.id", target = "industryId")
    @Mapping(source = "industry.name", target = "industryName")
    @Mapping(source = "user.id", target = "ownerId")
    @Mapping(source = "user.username", target = "ownerUsername")
    CompanyResponse companyToCompanyResponse(Company company);

}
