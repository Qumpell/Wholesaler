package pl.matkan.wholesaler.contactperson;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.company.CompanyService;
import pl.matkan.wholesaler.contactperson.mapper.ContactPersonMapper;
import pl.matkan.wholesaler.exception.BadRequestException;
import pl.matkan.wholesaler.exception.ResourceNotFoundException;
import pl.matkan.wholesaler.tradenote.TradeNote;
import pl.matkan.wholesaler.tradenote.TradeNoteResponse;
import pl.matkan.wholesaler.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service("contactPersonService")
@RequiredArgsConstructor
public class ContactPersonServiceImpl implements ContactPersonService {

    private final ContactPersonRepository contactPersonRepository;
    private final UserService userService;
    private final CompanyService companyService;


    @Override
    public ContactPersonResponse create(ContactPersonDetailedRequest dto) {

        ContactPerson contactPerson = ContactPersonMapper.INSTANCE.contactPersonRequestToContactPerson(dto);

        contactPerson = contactPersonRepository.save(validateAndSetOwnerAndCompany(contactPerson, dto.ownerId(), dto.companyId()));

        return ContactPersonMapper.INSTANCE.contactPersonToContactPersonResponse(contactPerson);
    }

    @Override
    public ContactPersonResponse update(Long id, ContactPersonDetailedRequest contactPersonRequest) {

        ContactPerson existingContactPerson = getOneById(id);
        existingContactPerson = updateExistingContactPerson(existingContactPerson, contactPersonRequest);

        return ContactPersonMapper.INSTANCE.contactPersonToContactPersonResponse(contactPersonRepository.save(existingContactPerson));
    }

    @Override
    public ContactPersonResponse findById(Long id) {
        return ContactPersonMapper.INSTANCE.contactPersonToContactPersonResponse(getOneById(id));
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
    public List<ContactPersonResponse> findAll() {
        List<ContactPerson> contactPeople = contactPersonRepository.findAll();
        return contactPeople.stream()
                .map(ContactPersonMapper.INSTANCE::contactPersonToContactPersonResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ContactPersonResponse> findAll(int pageNumber, int pageSize, String field, String order) {
        Page<ContactPerson> contactPeople = contactPersonRepository.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.Direction.fromString(order), field)
        );
        return contactPeople.map(ContactPersonMapper.INSTANCE::contactPersonToContactPersonResponse);
    }

    @Override
    public Page<ContactPersonResponse> findAllByUser(int pageNumber, int pageSize, String field, String order, Long userId) {
        Page<ContactPerson> contactPeople = contactPersonRepository.findByUserIdAndIsDeletedFalse(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.Direction.fromString(order), field), userId
        );
        return contactPeople.map(ContactPersonMapper.INSTANCE::contactPersonToContactPersonResponse);
    }

    private ContactPerson updateExistingContactPerson(ContactPerson contactPerson, ContactPersonDetailedRequest dto) {
        contactPerson.setFirstname(dto.firstname());
        contactPerson.setSurname(dto.surname());
        contactPerson.setMail(dto.mail());
        contactPerson.setPhoneNumber(dto.phoneNumber());
        contactPerson.setPosition(dto.position());

        return validateAndSetOwnerAndCompany(contactPerson, dto.ownerId(), dto.companyId());

    }

    private ContactPerson getOneById(Long id) {
        return contactPersonRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact person was not found", "with given id:= " + id));
    }

    private ContactPerson validateAndSetOwnerAndCompany(ContactPerson contactPerson, Long ownerId, Long companyId) {

        try {
            contactPerson.setUser(userService.getOneById(ownerId));
            contactPerson.setCompany(companyService.getOneById(companyId));
        } catch (ResourceNotFoundException e) {
            throw new BadRequestException("Invalid payload", e.getMessage() + " " + e.getErrorDetails());
        }

        return contactPerson;
    }
}
