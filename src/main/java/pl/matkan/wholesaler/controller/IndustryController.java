package pl.matkan.wholesaler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.matkan.wholesaler.model.Industry;
import pl.matkan.wholesaler.service.impl.IndustryServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/industries")
public class IndustryController {
    private final IndustryServiceImpl industryService;


    public IndustryController(IndustryServiceImpl industryService) {
        this.industryService = industryService;
    }

    /* READ */
    @GetMapping("/{id}")
    public ResponseEntity<Industry> getOne(@PathVariable("id") Long id) {
        Optional<Industry> one = industryService.findById(id);
        return one.map(industry -> new ResponseEntity<>(industry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping()
    public List<Industry> getAll() {
        return industryService.findAll();
    }

    /* CREATE */
    @PostMapping()
    public ResponseEntity<Industry> createOne(@RequestBody Industry one) {
        Industry industryOne = industryService.create(one);
        return new ResponseEntity<>(industryOne, HttpStatus.CREATED);
    }

    /* UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<Industry> updateOne(@PathVariable("id") Long id, @RequestBody Industry one) {
        if (industryService.existsById(id)) {
            Industry updatedOne = industryService.update(id, one);
            return new ResponseEntity<>(updatedOne, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /* DELETE */
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
