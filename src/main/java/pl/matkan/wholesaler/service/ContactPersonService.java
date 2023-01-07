package pl.matkan.wholesaler.service;

import pl.matkan.wholesaler.model.ContactPerson;

import java.util.List;
import java.util.Optional;

public interface ContactPersonService {
    public ContactPerson create(ContactPerson one);

    public ContactPerson update(Long id, ContactPerson one);

    Optional<ContactPerson> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    public List<ContactPerson> findAll();
}
