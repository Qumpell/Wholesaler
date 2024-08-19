package pl.matkan.wholesaler.company;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.company.mapper.CompanyRequestMapper;
import pl.matkan.wholesaler.company.mapper.CompanyResponseMapper;
import pl.matkan.wholesaler.exception.BadRequestException;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.industry.IndustryService;
import pl.matkan.wholesaler.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service("companyService")
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserService userService;
    private final IndustryService industryService;
    private final CompanyResponseMapper companyResponseMapper;
    private final CompanyRequestMapper companyRequestMapper;



    @Override
    public CompanyResponse create(CompanyRequest companyIn) {

        try {
            userService.existsByIdOrThrow(companyIn.ownerId());
            industryService.existsByNameOrThrow(companyIn.industryName());
//            Industry industry = industryService.getOneIndustryByName(companyIn.industryName());
//            User user = userService.getOneUserById(companyIn.ownerId());

//            companyToCreate.setIndustry(industry);
//            companyToCreate.setUser(user);

        }catch (EntityNotFoundException e) {
            throw new BadRequestException("Invalid payload", e.getMessage() + " " +  e.getErrorDetails());
        }
        Company companyToCreate = companyRequestMapper.companyRequestToCompany(companyIn);
        try{


            Company companyCreated = companyRepository.save(companyToCreate);
            return companyResponseMapper.companyToCompanyResponse(companyCreated);

        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Company with name:=" + companyToCreate.getName() + " already exists");
        }

    }

    @Override
    public CompanyResponse update(Long id, CompanyRequest companyRequest) {

        return companyRepository.findById(id).map(company -> {

            try {

                userService.existsByIdOrThrow(companyRequest.ownerId());
                industryService.existsByNameOrThrow(companyRequest.industryName());

            } catch (EntityNotFoundException e) {
                throw new BadRequestException("Invalid payload", e.getMessage() + " " + e.getErrorDetails());
            }

            company = companyRequestMapper.companyRequestToCompany(companyRequest);

            try {
                Company updatedCompany = companyRepository.save(company);

                return companyResponseMapper.companyToCompanyResponse(updatedCompany);

            }catch (DataIntegrityViolationException ex){
                throw new DataIntegrityViolationException("Company with name:=" + company.getName() + " already exists");
            }


        }).orElseThrow(() -> new EntityNotFoundException("Company was not found" ,"with id: " + id));
    }

//        Company companyToBeUpdated = getOneCompanyById(id);
//
//        companyToBeUpdated.setName(updatedCompanyResponse.getName());
//        companyToBeUpdated.setAddress(updatedCompanyResponse.getAddress());
//        companyToBeUpdated.setCity(updatedCompanyResponse.getCity());
//        companyToBeUpdated.setNip(updatedCompanyResponse.getNip());
//        companyToBeUpdated.setUser(userService.getOneUserById(updatedCompanyResponse.getOwnerId()));
//        companyToBeUpdated.setIndustry(industryService.getOneIndustryByName(updatedCompanyResponse.getIndustryName()));

//        return companyRepository.save(companyToBeUpdated);
//        return null;


    @Override
    public CompanyResponse findById(Long id) {
        return companyResponseMapper.companyToCompanyResponse(
                companyRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Company was not found", "with id:=" + id))
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
    public List<CompanyResponse> findAll() {
        List<Company> companies  = companyRepository.findAll();
        return companies.stream()
                .map(companyResponseMapper::companyToCompanyResponse)
                .collect(Collectors.toList());
    }
    public Page<CompanyResponse> findCompaniesWithPaginationAndSort(int pageNumber, int pageSize, String field, String order)
    {
        Page<Company> companies  = companyRepository.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field))
        );
        return companies.map(companyResponseMapper::companyToCompanyResponse);
    }

    @Override
    public void existsByNameOrThrow(String name) {
        if (!companyRepository.existsByName(name)) {
            throw new EntityNotFoundException("Company was not found", "with name:=" + name);
        }
    }

//    public Company getOneCompanyById(Long id) {
//        return companyRepository.findById(id)
//                .orElseThrow((() -> new EntityNotFoundException("Company not found", "with given id:= " + id))
//                );
//    }
}
