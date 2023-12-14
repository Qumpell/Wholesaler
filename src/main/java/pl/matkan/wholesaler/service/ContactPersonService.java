package pl.matkan.wholesaler.service;

import pl.matkan.wholesaler.dto.ContactPersonDto;
import pl.matkan.wholesaler.model.ContactPerson;

import java.util.List;

public interface ContactPersonService {
    ContactPerson create(ContactPersonDto one);

    ContactPerson update(Long id, ContactPersonDto one);

    ContactPersonDto findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<ContactPersonDto> findAll();
}
