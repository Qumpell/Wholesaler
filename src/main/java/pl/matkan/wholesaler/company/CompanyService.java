package pl.matkan.wholesaler.company;

import org.springframework.data.domain.Page;

import java.util.List;

public interface CompanyService {


    CompanyResponse create(CompanyDetailedRequest one);

    CompanyResponse update(Long id, CompanyDetailedRequest one);

    CompanyResponse findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<CompanyResponse> findAll();

    Page<CompanyResponse> findCompaniesWithPaginationAndSort(int pageNumber, int pageSize, String field, String order);

    Company getOneById(Long id);
}
