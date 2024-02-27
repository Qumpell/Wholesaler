package pl.matkan.wholesaler.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.matkan.wholesaler.dto.ContactPersonDto;
import pl.matkan.wholesaler.model.ContactPerson;
import pl.matkan.wholesaler.service.impl.ContactPersonServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/contactPersons")
public class ContactPersonController {

    private final ContactPersonServiceImpl contactPersonService;


    public ContactPersonController(ContactPersonServiceImpl contactPersonService) {
        this.contactPersonService = contactPersonService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactPersonDto> getOne(@PathVariable("id") Long id) {
        ContactPersonDto contactPersonDto = contactPersonService.findById(id);

        return new ResponseEntity<>(contactPersonDto, HttpStatus.OK);
    }

//    @GetMapping()
//    public ResponseEntity<List<ContactPersonDto>> getAll() {
//        return new ResponseEntity<>(contactPersonService.findAll(),HttpStatus.OK);
//    }
    @GetMapping()
    public ResponseEntity<Page<ContactPersonDto>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return new ResponseEntity<>(
                contactPersonService.findContactPeopleWithPaginationAndSort(pageNumber, pageSize, field, order),
                HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ContactPerson> createOne(@RequestBody ContactPersonDto one) {
        ContactPerson contactPersonOne = contactPersonService.create(one);
        return new ResponseEntity<>(contactPersonOne, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactPerson> updateOne(@PathVariable("id") Long id, @RequestBody ContactPersonDto one) {
        if (contactPersonService.existsById(id)) {
            ContactPerson updatedOne = contactPersonService.update(id, one);
            return new ResponseEntity<>(updatedOne, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Long> deleteOne(@PathVariable("id") Long id) {
        if (contactPersonService.existsById(id)) {
            contactPersonService.deleteById(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }


}
