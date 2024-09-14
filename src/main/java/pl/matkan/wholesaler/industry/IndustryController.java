package pl.matkan.wholesaler.industry;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/industries")
public class IndustryController {

    private final IndustryService industryService;


    @GetMapping("/{id}")
    public ResponseEntity<Industry> getOne(@PathVariable("id") Long id) {

        return new ResponseEntity<>(industryService.findById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<Industry>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return new ResponseEntity<>(
                industryService.findAll(pageNumber, pageSize, field, order),
                HttpStatus.OK
        );
    }
    @PostMapping()
    public ResponseEntity<Industry> createOne(@RequestBody IndustryRequest one) {
        return new ResponseEntity<>( industryService.create(one), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Industry> updateOne(@PathVariable("id") Long id, @RequestBody IndustryRequest one) {

        if (industryService.existsById(id))
        {
            return new ResponseEntity<>(industryService.update(id, one), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteOne(@PathVariable("id") Long id) {
        if (!industryService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        industryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
