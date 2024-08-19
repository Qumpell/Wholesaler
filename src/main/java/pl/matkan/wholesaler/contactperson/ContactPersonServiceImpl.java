package pl.matkan.wholesaler.contactperson;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.company.CompanyService;
import pl.matkan.wholesaler.contactperson.mapper.ContactPersonRequestMapper;
import pl.matkan.wholesaler.contactperson.mapper.ContactPersonResponseMapper;
import pl.matkan.wholesaler.exception.BadRequestException;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service("contactPersonService")
@RequiredArgsConstructor
public class ContactPersonServiceImpl implements ContactPersonService {

    private final ContactPersonRepository contactPersonRepository;
    private final ContactPersonResponseMapper responseMapper;
    private final ContactPersonRequestMapper requestMapper;
    private final UserService userService;
    private final CompanyService companyService;



    @Override
    public ContactPersonResponse create(ContactPersonRequest one) {

//
//
//        contactPersonToCreate.setCompany(companyService.getOneCompanyById(one.getCompanyId()));
//        contactPersonToCreate.setUser(userService.getOneUserById(one.getOwnerId()));

        try{
            userService.existsByIdOrThrow(one.ownerId());
            companyService.existsByNameOrThrow(one.companyName());

        }catch (EntityNotFoundException e){
            throw new BadRequestException("Invalid payload", e.getMessage() + " " +  e.getErrorDetails());
        }
        ContactPerson contactPersonToCreate = requestMapper.contactPersonRequestToContactPerson(one);

        return responseMapper.contactPersonToContactPersonResponse(contactPersonRepository.save(contactPersonToCreate));
    }

    @Override
    public ContactPersonResponse update(Long id, ContactPersonRequest one) {


//        contactPersonUpdated.setId(id);
//        contactPersonUpdated.setCompany(companyService.getOneCompanyById(one.getCompanyId()));
//        contactPersonUpdated.setUser(userService.getOneUserById(one.getOwnerId()));

      return contactPersonRepository.findById(id).map(cp ->{

          try{
              userService.existsByIdOrThrow(one.ownerId());
              companyService.existsByNameOrThrow(one.companyName());

          }catch (EntityNotFoundException e){
              throw new BadRequestException("Invalid payload", e.getMessage() + " " +  e.getErrorDetails());
          }

          ContactPerson contactPersonUpdated = requestMapper.contactPersonRequestToContactPerson(one);
          ContactPerson updatedContactPerson = contactPersonRepository.save(contactPersonUpdated);

          return responseMapper.contactPersonToContactPersonResponse(updatedContactPerson);

      }).orElseThrow(() -> new EntityNotFoundException("Contact person was not found" ,"with id: " + id));


    }
    @Override
    public ContactPersonResponse findById(Long id) {
        return responseMapper.contactPersonToContactPersonResponse(
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
    public List<ContactPersonResponse> findAll() {
        List<ContactPerson> contactPeople = contactPersonRepository.findAll();
        return contactPeople.stream()
                .map(responseMapper::contactPersonToContactPersonResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ContactPersonResponse> findContactPeopleWithPaginationAndSort(int pageNumber, int pageSize, String field, String order) {
        Page<ContactPerson> contactPeople = contactPersonRepository.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.Direction.fromString(order), field)
        );
        return contactPeople.map(responseMapper::contactPersonToContactPersonResponse);
    }
}
