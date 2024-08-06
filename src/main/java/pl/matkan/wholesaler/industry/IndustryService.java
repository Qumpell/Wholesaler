package pl.matkan.wholesaler.industry;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IndustryService {

    Industry create(Industry one);

    Industry update(Long id, Industry one);

    Industry findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<Industry> findAll();

    Page<Industry> findIndustriesWithPaginationAndSort(int pageNumber, int pageSize, String field, String order);
}
