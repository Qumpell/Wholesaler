package pl.matkan.wholesaler.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.model.Company;
import pl.matkan.wholesaler.model.Industry;
import pl.matkan.wholesaler.repo.CompanyRepository;
import pl.matkan.wholesaler.repo.IndustryRepository;
import pl.matkan.wholesaler.service.IndustryService;

import java.util.List;
import java.util.Optional;

@Service("industryService")
public class IndustryServiceImpl implements IndustryService {


    private final IndustryRepository industryRepo;
    private final CompanyRepository companyRepository;

    public IndustryServiceImpl(IndustryRepository industryRepo,
                               CompanyRepository companyRepository) {
        this.industryRepo = industryRepo;
        this.companyRepository = companyRepository;
    }

    @Override
    public Industry create(Industry one) {
        return industryRepo.save(one);
    }

    @Override
    public Industry update(Long id, Industry one) {
        one.setId(id);
        return industryRepo.save(one);
    }

    @Override
    public Optional<Industry> findById(Long id) {
        return industryRepo.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return industryRepo.existsById(id);
    }

    @Override
    public void deleteById(Long id) {

        Optional<Industry> industryOptional = findById(id);
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

    public Page<Industry> findIndustriesWithPaginationAndSort(int pageNumber, int pageSize, String field, String order) {
        return industryRepo.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field))
        );
    }
}
