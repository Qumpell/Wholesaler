package pl.matkan.wholesaler.contactperson;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.matkan.wholesaler.auth.AccessControlService;
import pl.matkan.wholesaler.auth.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/contact-persons")
public class ContactPersonController {

    private final ContactPersonService contactPersonService;
    private final AccessControlService accessControlService;

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

    @GetMapping("/{userId}/all")
    public ResponseEntity<Page<ContactPersonResponse>> getAllByUser(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order,
            @PathVariable Long userId) {

        return new ResponseEntity<>(
                contactPersonService.findAllByUser(pageNumber, pageSize, field, order, userId),
                HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ContactPersonResponse> createOne(
            @RequestBody @Valid ContactPersonRequest one,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ContactPersonDetailedRequest contactPersonDTO = new ContactPersonDetailedRequest(
                one.firstname(),
                one.surname(),
                one.phoneNumber(),
                one.mail(),
                one.position(),
                one.companyId(),
                userDetails.getId()
        );

        return new ResponseEntity<>(contactPersonService.create(contactPersonDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactPersonResponse> updateOne(
            @PathVariable("id") Long id,
            @RequestBody @Valid ContactPersonRequest one,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        final Long ownerId = contactPersonService.findById(id).ownerId();
        accessControlService.canAccessResource(ownerId, userDetails);
        ContactPersonDetailedRequest contactPersonDTO = new ContactPersonDetailedRequest(
                one.firstname(),
                one.surname(),
                one.phoneNumber(),
                one.mail(),
                one.position(),
                one.companyId(),
                ownerId
        );
        return new ResponseEntity<>(contactPersonService.update(id, contactPersonDTO), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        accessControlService.canAccessResource(contactPersonService.findById(id).ownerId(), userDetails);
        contactPersonService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
