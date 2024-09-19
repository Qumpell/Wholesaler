package pl.matkan.wholesaler.industry;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.company.Company;
import pl.matkan.wholesaler.company.CompanyRepository;
import pl.matkan.wholesaler.exception.ResourceNotFoundException;

import java.util.List;

@Service("industryService")
@RequiredArgsConstructor
public class IndustryServiceImpl implements IndustryService {


    private final IndustryRepository industryRepo;
    private final CompanyRepository companyRepository;

    @Override
    public Industry create(IndustryRequest one) {
        try {
            Industry industry = new Industry();
            industry.setName(one.name());

            return industryRepo.save(industry);

        }catch (DataIntegrityViolationException exception) {
            throw new DataIntegrityViolationException("Industry with name:=" + one.name() + " already exists");
        }

    }

    @Override
    public Industry update(Long id, IndustryRequest one) {
       return industryRepo.findById(id).map(ind ->{

            ind.setName(one.name());

           try {
                return industryRepo.save(ind);

           } catch (DataIntegrityViolationException exception) {
               throw new DataIntegrityViolationException("Industry with name:=" + one.name() + " already exists");
           }

       }).orElseThrow(() -> new ResourceNotFoundException("Industry was not found", "with id:=" + id));


//        try {
//            Industry industry = findById(id);
//            industry.setName(one.getName());
//            industry.setCompanies(one.getCompanies());
//            one.setId(id);
//            return industryRepo.save(industry);
//        }catch (DataIntegrityViolationException exception) {
//            throw new DataIntegrityViolationException("Industry with name:=" + one.name() + " already exists");
//        }

    }

    @Override
    public Industry findById(Long id) {
        return industryRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Industry was not found", "with id:=" + id)
        );
    }

    @Override
    public boolean existsById(Long id) {
        return industryRepo.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
//        Industry industryOptional = industryRepo.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Industry was not found", "with id:=" + id));
//       industryRepo.deleteById(id);


//        Optional<Industry> industryOptional = industryRepo.findById(id);
//        if (industryOptional.isPresent()){
            Industry industry = findById(id);
            List<Company> companies = industry.getCompanies();
            if(companies != null){
                for(Company company : companies){
                    company.setIndustry(null);
                }
                companyRepository.saveAll(companies);
            }
        industryRepo.deleteById(id);
    }

    @Override
    public List<Industry> findAll() {
        return industryRepo.findAll();
    }

    @Override
    public Page<Industry> findAll(int pageNumber, int pageSize, String field, String order) {
        return industryRepo.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field))
        );
    }

}
