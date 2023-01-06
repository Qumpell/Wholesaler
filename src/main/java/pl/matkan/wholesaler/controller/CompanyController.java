package pl.matkan.wholesaler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.matkan.wholesaler.model.Company;
import pl.matkan.wholesaler.service.CompanyService;


import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping(value = "/companies")

public class CompanyController {
    private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
        this.companyService= companyService;
    }

    /* READ */
    @GetMapping("/{id}")
    public ResponseEntity<Company> getOne(@PathVariable("id") Long id) {
        Optional<Company> one = companyService.findById(id);
        return one.map(role -> new ResponseEntity<>(role, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping()
    public List<Company> getAll() {
        return companyService.findAll();
    }
    /* CREATE */
    @PostMapping()
    public ResponseEntity<Company> createOne(@RequestBody Company one) {
        //System.out.println("co przyszlo:" + one.toString());
        Company roleOne = companyService.create(one);
        return new ResponseEntity<>(roleOne, HttpStatus.CREATED);
    }

    /* UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateOne(@PathVariable("id") Long id, @RequestBody Company one) {
        if (companyService.existsById(id)) {
            Company updatedOne = companyService.update(id, one);
            return new ResponseEntity<>(updatedOne, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /* DELETE */
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteOne(@PathVariable("id") Long id) {
        companyService.deleteById(id);
    }

}
