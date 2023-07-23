package pl.matkan.wholesaler.service.impl;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.model.ContactPerson;
import pl.matkan.wholesaler.repo.ContactPersonRepository;
import pl.matkan.wholesaler.service.ContactPersonService;

import java.util.List;
import java.util.Optional;

@Service("contactPersonService")
@Repository
public class ContactPersonServiceImpl implements ContactPersonService {

    private final ContactPersonRepository contactPersonRepository;

    public ContactPersonServiceImpl(ContactPersonRepository contactPersonRepository) {
        this.contactPersonRepository = contactPersonRepository;
    }

    @Override
    public ContactPerson create(ContactPerson one) {
        return contactPersonRepository.save(one);
    }

    @Override
    public ContactPerson update(Long id, ContactPerson one) {
        one.setId(id);
        return contactPersonRepository.save(one);
    }

    @Override
    public Optional<ContactPerson> findById(Long id) {
        return contactPersonRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return contactPersonRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        contactPersonRepository.deleteById(id);
    }

    @Override
    public List<ContactPerson> findAll() {
        return contactPersonRepository.findAll();
    }
}
