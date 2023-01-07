package pl.matkan.wholesaler.service.impl;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.model.Industry;
import pl.matkan.wholesaler.repo.IndustryRepository;
import pl.matkan.wholesaler.service.IndustryService;

import java.util.List;
import java.util.Optional;

@Service("industryService")
@Repository
public class IndustryServiceImpl implements IndustryService {


    private final IndustryRepository industryRepo;

    public IndustryServiceImpl(IndustryRepository industryRepo) {
        this.industryRepo = industryRepo;
    }

    @Override
    public Industry create(Industry one) {
        Industry savedOne = industryRepo.save(one);
        return savedOne;
    }

    @Override
    public Industry update(Long id, Industry one) {

        one.setId(id);
        Industry savedOne = industryRepo.save(one);
        return savedOne;
    }

    @Override
    public Optional<Industry> findById(Long id) {
        Optional<Industry> one = industryRepo.findById(id);
        if (one.isPresent()) {
            //	Customer dto = modelMapper.map(one.get(), EmployeeDTO.class);
            return one;
        } else
            return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return industryRepo.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        industryRepo.deleteById(id);
    }

    @Override
    public List<Industry> findAll() {
        List<Industry> all = industryRepo.findAll();
        System.out.println("company service:=" + industryRepo.findAll());
        return all;
    }
}
