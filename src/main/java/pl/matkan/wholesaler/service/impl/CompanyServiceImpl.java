package pl.matkan.wholesaler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.model.Company;
import pl.matkan.wholesaler.repo.CompanyRepository;
import pl.matkan.wholesaler.service.CompanyService;

import java.util.List;
import java.util.Optional;

@Service("companyService")
@Repository
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepo;

    @Override
    public Company create(Company one) {
        Company savedOne = companyRepo.save(one);
        return savedOne;
    }

    @Override
    public Company update(Long id, Company one) {

        one.setId(id);
        Company savedOne = companyRepo.save(one);
        return savedOne;
    }

    @Override
    public Optional<Company> findById(Long id) {
        Optional<Company> one = companyRepo.findById(id);
        if (one.isPresent()) {
            //	Customer dto = modelMapper.map(one.get(), EmployeeDTO.class);
            return one;
        } else
            return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return companyRepo.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        companyRepo.deleteById(id);
    }

    @Override
    public List<Company> findAll() {
        List<Company> all = companyRepo.findAll();
        System.out.println("company service:=" + companyRepo.findAll());
        return all;
    }
}
