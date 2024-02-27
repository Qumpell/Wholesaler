package pl.matkan.wholesaler.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.dto.ContactPersonDto;
import pl.matkan.wholesaler.dto.mapper.ContactPersonMapper;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
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
    private final CompanyServiceImpl companyService;
    private final UserServiceImpl userService;

    public ContactPersonServiceImpl(ContactPersonRepository contactPersonRepository, ContactPersonMapper contactPersonMapper, CompanyServiceImpl companyService, UserServiceImpl userService) {
        this.contactPersonRepository = contactPersonRepository;
        this.contactPersonMapper = contactPersonMapper;
        this.companyService = companyService;
        this.userService = userService;
    }

    @Override
    public ContactPerson create(ContactPersonDto one) {
        ContactPerson contactPersonToCreate = contactPersonMapper.contactPersonDtoToContactPerson(one);

        contactPersonToCreate.setCompany(companyService.getOneCompanyById(one.getCompanyId()));
        contactPersonToCreate.setUser(userService.getOneUserById(one.getOwnerId()));

        return contactPersonRepository.save(contactPersonToCreate);
    }

    @Override
    public ContactPerson update(Long id, ContactPersonDto one) {
        ContactPerson contactPersonUpdated = contactPersonMapper.contactPersonDtoToContactPerson(one);

        contactPersonUpdated.setId(id);
        contactPersonUpdated.setCompany(companyService.getOneCompanyById(one.getCompanyId()));
        contactPersonUpdated.setUser(userService.getOneUserById(one.getOwnerId()));

        return contactPersonRepository.save(contactPersonUpdated);
    }
    @Override
    public ContactPersonDto findById(Long id) {
        return contactPersonMapper.contactPersonToContactPersonDto(
                contactPersonRepository.findById(id).
                        orElseThrow(()->new EntityNotFoundException("Contact person was not found", "with given id:= " + id))
        );
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

    public Page<ContactPersonDto> findContactPeopleWithPaginationAndSort(int pageNumber, int pageSize, String field, String order) {
        Page<ContactPerson> contactPeople = contactPersonRepository.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.Direction.fromString(order), field)
        );
        return contactPeople.map(contactPersonMapper::contactPersonToContactPersonDto);
    }
}
