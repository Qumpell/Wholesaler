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
    public ResponseEntity<ContactPersonResponse> getOne(@PathVariable("id") Long id) {

        return new ResponseEntity<>(contactPersonService.findById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<ContactPersonResponse>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return new ResponseEntity<>(
                contactPersonService.findAll(
                        pageNumber,
                        pageSize,
                        field,
                        order),
                HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ContactPersonResponse> createOne(@RequestBody ContactPersonRequest one) {

        return new ResponseEntity<>(contactPersonService.create(one), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus( HttpStatus.OK)
    public ResponseEntity<ContactPersonResponse> updateOne(@PathVariable("id") Long id, @RequestBody ContactPersonRequest one) {
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
