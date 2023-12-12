package pl.matkan.wholesaler.service;

import pl.matkan.wholesaler.dto.CompanyDto;
import pl.matkan.wholesaler.model.Company;

import java.util.List;

public interface CompanyService {

    Company create(CompanyDto one);

    Company update(Long id, CompanyDto one);

    CompanyDto findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

   List<CompanyDto> findAll();
}
