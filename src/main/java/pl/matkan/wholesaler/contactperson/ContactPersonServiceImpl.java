package pl.matkan.wholesaler.contactperson;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.company.CompanyServiceImpl;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.user.UserServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service("contactPersonService")
@RequiredArgsConstructor
public class ContactPersonServiceImpl implements ContactPersonService {

    private final ContactPersonRepository contactPersonRepository;
    private final ContactPersonMapper contactPersonMapper;
    private final CompanyServiceImpl companyService;
    private final UserServiceImpl userService;


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

    @Override
    public Page<ContactPersonDto> findContactPeopleWithPaginationAndSort(int pageNumber, int pageSize, String field, String order) {
        Page<ContactPerson> contactPeople = contactPersonRepository.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.Direction.fromString(order), field)
        );
        return contactPeople.map(contactPersonMapper::contactPersonToContactPersonDto);
    }
}
