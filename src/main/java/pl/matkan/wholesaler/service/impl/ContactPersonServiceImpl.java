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
        ContactPerson savedOne = contactPersonRepository.save(one);
        return savedOne;
    }

    @Override
    public ContactPerson update(Long id, ContactPerson one) {

        one.setId(id);
        ContactPerson savedOne = contactPersonRepository.save(one);
        return savedOne;
    }

    @Override
    public Optional<ContactPerson> findById(Long id) {
        Optional<ContactPerson> one = contactPersonRepository.findById(id);
        if (one.isPresent()) {
            return one;
        } else
            return Optional.empty();
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
        List<ContactPerson> all = contactPersonRepository.findAll();
        return all;
    }
}
