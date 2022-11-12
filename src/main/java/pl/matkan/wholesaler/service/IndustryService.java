package pl.matkan.wholesaler.service;

import pl.matkan.wholesaler.model.Industry;

import java.util.List;
import java.util.Optional;

public interface IndustryService {

    public Industry create(Industry one);

    public Industry update(Long id, Industry one);

    Optional<Industry> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    public List<Industry> findAll();
}
