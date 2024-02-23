package pl.matkan.wholesaler.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.matkan.wholesaler.dto.CompanyDto;
import pl.matkan.wholesaler.model.Company;
import pl.matkan.wholesaler.service.impl.CompanyServiceImpl;

import java.util.List;

@RestController
@RequestMapping(value = "/companies")
public class CompanyController {
    private final CompanyServiceImpl companyService;

    public CompanyController(CompanyServiceImpl companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getOne(@PathVariable("id") Long id) {
        CompanyDto companyDto = companyService.findById(id);
        return new ResponseEntity<>(companyDto, HttpStatus.OK);
    }

//    @GetMapping()
//    public ResponseEntity<List<CompanyDto>> getAll() {
//        return new ResponseEntity<>(companyService.findAll(), HttpStatus.OK);
//    }
    @GetMapping("/pagination/{offset}/{pageSize}")
    public ResponseEntity<Page<CompanyDto>> getCompaniesWithPagination(
            @PathVariable int offset, @PathVariable int pageSize
    ) {
        return new ResponseEntity<>(companyService.findCompaniesWithPagination(offset, pageSize), HttpStatus.OK);
    }
//    @GetMapping("/pagination/{offset}/{pageSize}/{field}")
    @GetMapping()
//    public ResponseEntity<Page<CompanyDto>> getCompaniesWithPaginationAndSort(
    public ResponseEntity<Page<CompanyDto>> getAll(
//            @PathVariable int offset, @PathVariable int pageSize, @PathVariable String field
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return new ResponseEntity<>(companyService.findCompaniesWithPaginationAndSort(offset, pageSize, sortBy), HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<Company> createOne(@RequestBody CompanyDto one) {
        Company companyOne = companyService.create(one);
        return new ResponseEntity<>(companyOne, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateOne(@PathVariable("id") Long id, @RequestBody CompanyDto one) {
        if (companyService.existsById(id)) {
            Company updatedCompany = companyService.update(id, one);
            return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Long> deleteOne(@PathVariable("id") Long id) {
        if (companyService.existsById(id)) {
            companyService.deleteById(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

}
