package pl.matkan.wholesaler.service.impl;

import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.dto.ContactPersonDto;
import pl.matkan.wholesaler.dto.mapper.ContactPersonMapper;
import pl.matkan.wholesaler.model.ContactPerson;
import pl.matkan.wholesaler.repo.ContactPersonRepository;
import pl.matkan.wholesaler.service.ContactPersonService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("contactPersonService")
public class ContactPersonServiceImpl implements ContactPersonService {

    private final ContactPersonRepository contactPersonRepository;
    private final ContactPersonMapper contactPersonMapper;

    public ContactPersonServiceImpl(ContactPersonRepository contactPersonRepository, ContactPersonMapper contactPersonMapper) {
        this.contactPersonRepository = contactPersonRepository;
        this.contactPersonMapper = contactPersonMapper;
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
    public List<ContactPersonDto> findAll() {
        List<ContactPerson> contactPeople = contactPersonRepository.findAll();
        return contactPeople.stream()
                .map(contactPersonMapper::contactPersonToContactPersonDto)
                .collect(Collectors.toList());
    }
}
