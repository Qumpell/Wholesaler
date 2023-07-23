package pl.matkan.wholesaler.service.impl;

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

    private final CompanyRepository companyRepo;

    public CompanyServiceImpl(CompanyRepository companyRepo) {
        this.companyRepo = companyRepo;
    }

    @Override
    public Company create(Company one) {
        return companyRepo.save(one);
    }

    @Override
    public Company update(Long id, Company one) {
        one.setId(id);
        return companyRepo.save(one);
    }

    @Override
    public Optional<Company> findById(Long id) {
        return companyRepo.findById(id);
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
