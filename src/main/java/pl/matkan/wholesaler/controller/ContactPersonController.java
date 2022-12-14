package pl.matkan.wholesaler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    /* READ */
    @GetMapping("/{id}")
    public ResponseEntity<ContactPerson> getOne(@PathVariable("id") Long id) {
        Optional<ContactPerson> one = contactPersonService.findById(id);
        return one.map(contactPerson -> new ResponseEntity<>(contactPerson, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping()
    public List<ContactPerson> getAll() {
        return contactPersonService.findAll();
    }

    /* CREATE */
    @PostMapping()
    public ResponseEntity<ContactPerson> createOne(@RequestBody ContactPerson one) {
        ContactPerson contactPersonOne = contactPersonService.create(one);
        return new ResponseEntity<>(contactPersonOne, HttpStatus.CREATED);
    }

    /* UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<ContactPerson> updateOne(@PathVariable("id") Long id, @RequestBody ContactPerson one) {
        if (contactPersonService.existsById(id)) {
            ContactPerson updatedOne = contactPersonService.update(id, one);
            return new ResponseEntity<>(updatedOne, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /* DELETE */
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
