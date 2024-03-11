package pl.matkan.wholesaler.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.matkan.wholesaler.model.Industry;
import pl.matkan.wholesaler.service.impl.IndustryServiceImpl;

import java.util.Optional;

@RestController
@RequestMapping(value = "/industries")
public class IndustryController {
    private final IndustryServiceImpl industryService;

    public IndustryController(IndustryServiceImpl industryService) {
        this.industryService = industryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Industry> getOne(@PathVariable("id") Long id) {
        Optional<Industry> one = industryService.findById(id);
        return one.map(industry -> new ResponseEntity<>(industry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
//
//    @GetMapping()
//    public List<Industry> getAll() {
//        return industryService.findAll();
//    }

    @GetMapping()
    public ResponseEntity<Page<Industry>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return new ResponseEntity<>(
                industryService.findIndustriesWithPaginationAndSort(pageNumber, pageSize, field, order),
                HttpStatus.OK
        );
    }
    @PostMapping()
    public ResponseEntity<Industry> createOne(@RequestBody Industry one) {
        Industry industryOne = industryService.create(one);
        return new ResponseEntity<>(industryOne, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Industry> updateOne(@PathVariable("id") Long id, @RequestBody Industry one) {
        if (industryService.existsById(id)) {
            Industry updatedOne = industryService.update(id, one);
            return new ResponseEntity<>(updatedOne, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Long> deleteOne(@PathVariable("id") Long id) {
        if (industryService.existsById(id)) {
            industryService.deleteById(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

}
