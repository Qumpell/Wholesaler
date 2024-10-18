package pl.matkan.wholesaler.company;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.matkan.wholesaler.auth.UserDetailsImpl;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getOne(@PathVariable("id") Long id) {
        return new ResponseEntity<>(companyService.findById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<CompanyResponse>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return new ResponseEntity<>(
                companyService.findCompaniesWithPaginationAndSort(pageNumber, pageSize, field, order),
                HttpStatus.OK
        );
    }

    @PostMapping()
    public ResponseEntity<CompanyResponse> createOne(
            @RequestBody @Valid CompanyRequest one,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CompanyDetailedRequest companyDetailedRequest = new CompanyDetailedRequest(
                one.nip(),
                one.regon(),
                one.name(),
                one.address(),
                one.city(),
                one.industryId(),
                userDetails.getId()
        );
        return new ResponseEntity<>(companyService.create(companyDetailedRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CompanyResponse> updateOne(
            @PathVariable("id") Long id,
            @RequestBody @Valid CompanyRequest one) {

        final Long ownerId = companyService.findById(id).ownerId();
        CompanyDetailedRequest companyDetailedRequest = new CompanyDetailedRequest(
                one.nip(),
                one.regon(),
                one.name(),
                one.address(),
                one.city(),
                one.industryId(),
                ownerId
        );
        return new ResponseEntity<>(companyService.update(id, companyDetailedRequest), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteOne(@PathVariable("id") Long id) {
        if (!companyService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        companyService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
