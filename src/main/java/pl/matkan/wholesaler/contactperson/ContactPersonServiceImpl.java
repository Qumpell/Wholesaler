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
import pl.matkan.wholesaler.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service("contactPersonService")
@RequiredArgsConstructor
public class ContactPersonServiceImpl implements ContactPersonService {

    private final ContactPersonRepository contactPersonRepository;
//    private final ContactPersonResponseMapper responseMapper;
//    private final ContactPersonRequestMapper requestMapper;
    private final UserService userService;
    private final CompanyService companyService;
    private final ContactPersonMapper contactPersonMapper;



    @Override
    public ContactPersonResponse create(ContactPersonRequest dto) {

        ContactPerson contactPerson = contactPersonMapper.contactPersonRequestToContactPerson(dto);

        contactPerson = contactPersonRepository.save(validateAndSetOwnerAndCompany(contactPerson, dto.ownerId(), dto.companyId()));

        return contactPersonMapper.contactPersonToContactPersonResponse(contactPerson);
    }

    @Override
    public ContactPersonResponse update(Long id, ContactPersonRequest contactPersonRequest) {

        ContactPerson existingContactPerson = getOneById(id);
        existingContactPerson = updateExistingContactPerson(existingContactPerson, contactPersonRequest);

        return contactPersonMapper.contactPersonToContactPersonResponse(contactPersonRepository.save(existingContactPerson));

//        contactPersonUpdated.setId(id);
//        contactPersonUpdated.setCompany(companyService.getOneCompanyById(one.getCompanyId()));
//        contactPersonUpdated.setUser(userService.getOneUserById(one.getOwnerId()));

//      return contactPersonRepository.findById(id).map(cp ->{
//
//          try{
//              userService.existsByIdOrThrow(one.ownerId());
//              companyService.existsByNameOrThrow(one.companyName());
//
//          }catch (EntityNotFoundException e){
//              throw new BadRequestException("Invalid payload", e.getMessage() + " " +  e.getErrorDetails());
//          }
//
//          ContactPerson contactPersonUpdated = requestMapper.contactPersonRequestToContactPerson(one);
//          ContactPerson updatedContactPerson = contactPersonRepository.save(contactPersonUpdated);
//
//          return responseMapper.contactPersonToContactPersonResponse(updatedContactPerson);
//
//      }).orElseThrow(() -> new EntityNotFoundException("Contact person was not found" ,"with id: " + id));
    }
    @Override
    public ContactPersonResponse findById(Long id) {
//        return responseMapper.contactPersonToContactPersonResponse(getOneById(id));
        return contactPersonMapper.contactPersonToContactPersonResponse(getOneById(id));
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
//                .map(responseMapper::contactPersonToContactPersonResponse)
                .map(contactPersonMapper::contactPersonToContactPersonResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ContactPersonResponse> findAll(int pageNumber, int pageSize, String field, String order) {
        Page<ContactPerson> contactPeople = contactPersonRepository.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.Direction.fromString(order), field)
        );
//        return contactPeople.map(responseMapper::contactPersonToContactPersonResponse);
        return contactPeople.map(contactPersonMapper::contactPersonToContactPersonResponse);
    }

    private ContactPerson updateExistingContactPerson(ContactPerson contactPerson, ContactPersonRequest dto){
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
                .orElseThrow(()->new ResourceNotFoundException("Contact person was not found", "with given id:= " + id));
    }
    private ContactPerson validateAndSetOwnerAndCompany(ContactPerson contactPerson, Long ownerId, Long companyId) {

        try {
            contactPerson.setUser(userService.getOneById(ownerId));
            contactPerson.setCompany(companyService.getOneById(companyId));
        }
        catch (ResourceNotFoundException e){
            throw new BadRequestException("Invalid payload", e.getMessage() + " " +  e.getErrorDetails());
        }

        return contactPerson;
    }
}
