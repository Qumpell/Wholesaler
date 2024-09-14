package pl.matkan.wholesaler.company;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.company.mapper.CompanyMapper;
import pl.matkan.wholesaler.exception.BadRequestException;
import pl.matkan.wholesaler.exception.ResourceNotFoundException;
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
//    private final CompanyResponseMapper companyResponseMapper;
//    private final CompanyRequestMapper companyRequestMapper;
//    private final TradeNoteRepository tradeNoteRepository;
//    private final ContactPersonRepository contactPersonRepository;
    private final CompanyMapper companyMapper;

    @Override
    public CompanyResponse create(CompanyRequest companyRequest) {

        Company company = companyMapper.companyRequestToCompany(companyRequest);

//        validateOwnerAndIndustry(companyIn);
//
//        Company companyToCreate = companyRequestMapper.companyRequestToCompany(companyIn);
        company = validateAndSetOwnerAndIndustry(company, companyRequest.ownerId(), companyRequest.industryId());

//        try {
//
////            Company companyCreated = companyRepository.save(companyToCreate);
////            return companyResponseMapper.companyToCompanyResponse(companyCreated);
//            companyRepository.save(company);
//
//        } catch (DataIntegrityViolationException e) {
//            throw new DataIntegrityViolationException("Company with name:=" + company.getName() + " already exists");
//        }

        company = validateUniqueFields(company);

        return companyMapper.companyToCompanyResponse(company);
    }

    @Override
    public CompanyResponse update(Long id, CompanyRequest companyRequest) {

        Company companyFetched = getOneById(id);

        companyFetched = updateExistingCompany(companyFetched, companyRequest);


//        validateOwnerAndIndustry(companyRequest);

//        companyRequestMapper.updateCompanyFromRequest(companyRequest, company);

//        try {
////            company = companyRepository.save(company);
//            companyRepository.save(companyFetched);
//
//        } catch (DataIntegrityViolationException ex) {
//            throw new DataIntegrityViolationException("Company with name:=" + companyFetched.getName() + " already exists");
//        }

//        return companyResponseMapper.companyToCompanyResponse(company);
        companyFetched = validateUniqueFields(companyFetched);

        return companyMapper.companyToCompanyResponse(companyFetched);

    }

    private Company updateExistingCompany(Company existingCompany, CompanyRequest companyRequest) {
        existingCompany.setNip(companyRequest.nip());
        existingCompany.setRegon(companyRequest.regon());
        existingCompany.setName(companyRequest.name());
        existingCompany.setCity(companyRequest.city());
        existingCompany.setAddress(companyRequest.address());

        existingCompany = validateAndSetOwnerAndIndustry(existingCompany, companyRequest.ownerId(), companyRequest.industryId());

        return existingCompany;
    }

    @Override
    public CompanyResponse findById(Long id) {
//        return companyResponseMapper.companyToCompanyResponse(getCompanyById(id));
        return companyMapper.companyToCompanyResponse(getOneById(id));
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
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
//                .map(companyResponseMapper::companyToCompanyResponse)
                .map(companyMapper::companyToCompanyResponse)
                .collect(Collectors.toList());
    }

    public Page<CompanyResponse> findCompaniesWithPaginationAndSort(int pageNumber, int pageSize, String field, String order) {
        Page<Company> companies = companyRepository.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field))
        );
//        return companies.map(companyResponseMapper::companyToCompanyResponse);
        return companies.map(companyMapper::companyToCompanyResponse);
    }


    @Override
    public Company getOneById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow((() -> new ResourceNotFoundException("Company was not found", "with given id:= " + id))
                );
    }

    private Company validateUniqueFields(Company company) {

        try {
            return companyRepository.save(company);

        } catch (DataIntegrityViolationException ex) {
            String message = ex.getMostSpecificCause().getMessage();
            if (message.contains("nip")) {
                throw new DataIntegrityViolationException("Company with nip:=" + company.getNip() + " already exists");
            } else if (message.contains("regon")) {
                throw new DataIntegrityViolationException("Company with regon:=" + company.getRegon() + " already exists");

            } else if (message.contains("name")) {
                throw new DataIntegrityViolationException("Company with name:=" + company.getName() + " already exists");
            } else {
                throw new DataIntegrityViolationException("Company" + " already exists");
            }
        }
    }
    private Company validateAndSetOwnerAndIndustry(Company company, Long ownerId, Long industryId) {
        try {
            company.setUser(userService.getOneById(ownerId));
            company.setIndustry(industryService.findById(industryId));
        }
        catch (ResourceNotFoundException e){
            throw new BadRequestException("Invalid payload", e.getMessage() + " " +  e.getErrorDetails());
        }

        return company;
    }
}
