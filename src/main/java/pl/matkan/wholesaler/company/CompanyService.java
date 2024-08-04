package pl.matkan.wholesaler.company;

import org.springframework.data.domain.Page;

import java.util.List;

public interface CompanyService {

    Company create(CompanyDto one);

    Company update(Long id, CompanyDto one);

    CompanyDto findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<CompanyDto> findAll();

    Page<CompanyDto> findCompaniesWithPaginationAndSort(int pageNumber, int pageSize, String field, String order);
}
