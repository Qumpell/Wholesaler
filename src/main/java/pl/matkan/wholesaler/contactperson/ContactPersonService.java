package pl.matkan.wholesaler.contactperson;

import org.springframework.data.domain.Page;

import java.util.List;

public interface ContactPersonService {
    ContactPerson create(ContactPersonDto one);

    ContactPerson update(Long id, ContactPersonDto one);

    ContactPersonDto findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<ContactPersonDto> findAll();

    Page<ContactPersonDto> findContactPeopleWithPaginationAndSort(int pageNumber, int pageSize, String field, String order);
}
