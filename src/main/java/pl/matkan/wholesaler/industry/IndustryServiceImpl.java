package pl.matkan.wholesaler.industry;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.company.Company;
import pl.matkan.wholesaler.company.CompanyRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("industryService")
@RequiredArgsConstructor
public class IndustryServiceImpl implements IndustryService {


    private final IndustryRepository industryRepo;
    private final CompanyRepository companyRepository;


    @Override
    public Industry create(Industry one) {
        try {
            return industryRepo.save(one);
        }catch (DataIntegrityViolationException exception) {
            throw new DataIntegrityViolationException("Industry with name:=" + one.getName() + " already exists");
        }

    }

    @Override
    public Industry update(Long id, Industry one) {
        try {
            Industry industry = findById(id);
            industry.setName(one.getName());
            industry.setCompanies(one.getCompanies());
//            one.setId(id);
            return industryRepo.save(industry);
        }catch (DataIntegrityViolationException exception) {
            throw new DataIntegrityViolationException("Industry with name:=" + one.getName() + " already exists");
        }

    }

    @Override
    public Industry findById(Long id) {
        return industryRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Industry not found", "with id:=" + id)
        );
    }

    @Override
    public boolean existsById(Long id) {
        return industryRepo.existsById(id);
    }

    @Override
    public void deleteById(Long id) {

        Optional<Industry> industryOptional = industryRepo.findById(id);
        if (industryOptional.isPresent()){
            Industry industry = industryOptional.get();
            List<Company> companies = industry.getCompanies();
            if(companies != null){
                for(Company company : companies){
                    company.setIndustry(null);
                }
                companyRepository.saveAll(companies);
            }
        }
        industryRepo.deleteById(id);
    }

    @Override
    public List<Industry> findAll() {
        List<Industry> all = industryRepo.findAll();
        System.out.println("company service:=" + industryRepo.findAll());
        return all;
    }

    public Industry getOneIndustryByName(String industryName) {
        return industryRepo.findByName(industryName)
                .orElseThrow(
                        () -> new EntityNotFoundException("Industry was not found ",
                                "with given name:= " + industryName)
                );
    }

    @Override
    public Page<Industry> findIndustriesWithPaginationAndSort(int pageNumber, int pageSize, String field, String order) {
        return industryRepo.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field))
        );
    }
}
