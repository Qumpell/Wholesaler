package pl.matkan.wholesaler.service;

import pl.matkan.wholesaler.dto.CompanyDto;
import pl.matkan.wholesaler.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    public Company create(Company one);

    public Company update(Long id, Company one);

    Optional<Company> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    public List<CompanyDto> findAll();
}
