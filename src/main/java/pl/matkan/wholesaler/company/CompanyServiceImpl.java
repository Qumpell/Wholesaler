package pl.matkan.wholesaler.company;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.exception.BadRequestException;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.industry.IndustryServiceImpl;
import pl.matkan.wholesaler.user.UserServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service("companyService")
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserServiceImpl userService;
    private final CompanyMapper companyMapper;
    private final CompanyRequestMapper companyRequestMapper;
    private final IndustryServiceImpl industryService;


    @Override
    public Company create(CompanyRequest companyIn) {

        Company companyToCreate = companyRequestMapper.companyDtoToCompany(companyIn);

        try {
            companyToCreate.setIndustry(industryService.getOneIndustryByName(companyIn.industryName()));
            companyToCreate.setUser(userService.getOneUserById(companyIn.ownerId()));
        }catch (EntityNotFoundException e) {
            throw new BadRequestException("Invalid payload", e.getMessage() + " " +  e.getErrorDetails());
        }


        return companyRepository.save(companyToCreate);
    }

    @Override
    public Company update(Long id, CompanyDto updatedCompanyDto) {


//        companyRepository.findById(id).map(company -> {
//
//            company = companyMapper.companyDtoToCompany(updatedCompanyDto);
//            company.setUser(userService.getOneUserById(updatedCompanyDto.getOwnerId()));
//            company.setIndustry(industryService.getOneIndustryByName(updatedCompanyDto.getIndustryName()));
//            return companyRepository.save(company);
//
//        });

        Company companyToBeUpdated = getOneCompanyById(id);

        companyToBeUpdated.setName(updatedCompanyDto.getName());
        companyToBeUpdated.setAddress(updatedCompanyDto.getAddress());
        companyToBeUpdated.setCity(updatedCompanyDto.getCity());
        companyToBeUpdated.setNip(updatedCompanyDto.getNip());
        companyToBeUpdated.setUser(userService.getOneUserById(updatedCompanyDto.getOwnerId()));
        companyToBeUpdated.setIndustry(industryService.getOneIndustryByName(updatedCompanyDto.getIndustryName()));

        return companyRepository.save(companyToBeUpdated);
    }

    @Override
    public CompanyDto findById(Long id) {
        return companyMapper.companyToCompanyDto(
                companyRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Company not found", "with id:=" + id))
        );
    }

    @Override
    public boolean existsById(Long id) {
        return companyRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public List<CompanyDto> findAll() {
        List<Company> companies  = companyRepository.findAll();
        return companies.stream()
                .map(companyMapper::companyToCompanyDto)
                .collect(Collectors.toList());
    }
    public Page<CompanyDto> findCompaniesWithPaginationAndSort(int pageNumber, int pageSize, String field, String order)
    {
        Page<Company> companies  = companyRepository.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field))
        );
        return companies.map(companyMapper::companyToCompanyDto);
    }


    public Company getOneCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow((() -> new EntityNotFoundException("Company not found", "with given id:= " + id))
                );
    }
}
