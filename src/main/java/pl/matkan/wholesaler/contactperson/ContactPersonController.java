package pl.matkan.wholesaler.contactperson;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/contact-persons")
public class ContactPersonController {

    private final ContactPersonService contactPersonService;


    @GetMapping("/{id}")
    public ResponseEntity<ContactPersonDto> getOne(@PathVariable("id") Long id) {
        ContactPersonDto contactPersonDto = contactPersonService.findById(id);

        return new ResponseEntity<>(contactPersonDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<ContactPersonDto>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return new ResponseEntity<>(
                contactPersonService.findContactPeopleWithPaginationAndSort(
                        pageNumber,
                        pageSize,
                        field,
                        order),
                HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ContactPerson> createOne(@RequestBody ContactPersonDto one) {

        ContactPerson contactPersonOne = contactPersonService.create(one);
        return new ResponseEntity<>(contactPersonOne, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus( HttpStatus.OK)
    public ResponseEntity<ContactPerson> updateOne(@PathVariable("id") Long id, @RequestBody ContactPersonDto one) {

        if (!contactPersonService.existsById(id)) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(contactPersonService.update(id, one), HttpStatus.OK);

    }
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteOne(@PathVariable("id") Long id) {

        if (!contactPersonService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        contactPersonService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
