package pl.matkan.wholesaler.company;

import org.springframework.data.domain.Page;

import java.util.List;

public interface CompanyService {


    CompanyResponse create(CompanyRequest one);

    CompanyResponse update(Long id, CompanyRequest one);

    CompanyResponse findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<CompanyResponse> findAll();

    Page<CompanyResponse> findCompaniesWithPaginationAndSort(int pageNumber, int pageSize, String field, String order);

    void existsByNameOrThrow(String name);
}
