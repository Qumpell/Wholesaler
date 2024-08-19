package pl.matkan.wholesaler.contactperson;

import org.springframework.data.domain.Page;

import java.util.List;

public interface ContactPersonService {
    ContactPersonResponse create(ContactPersonRequest one);

    ContactPersonResponse update(Long id, ContactPersonRequest one);

    ContactPersonResponse findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<ContactPersonResponse> findAll();

    Page<ContactPersonResponse> findContactPeopleWithPaginationAndSort(int pageNumber, int pageSize, String field, String order);
}
