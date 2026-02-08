package pl.matkan.wholesaler.contactperson;

import org.springframework.data.domain.Page;

import java.util.List;

public interface ContactPersonService {
    ContactPersonResponse create(ContactPersonDetailedRequest one);

    ContactPersonResponse update(Long id, ContactPersonDetailedRequest one);

    ContactPersonResponse findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<ContactPersonResponse> findAll();

    Page<ContactPersonResponse> findAll(int pageNumber, int pageSize, String field, String order);

    Page<ContactPersonResponse> findAllByUser(int pageNumber, int pageSize, String field, String order, Long userId);
}
