package pl.matkan.wholesaler.role;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/roles")
public class RoleController {

    private final RoleService roleService;


    @GetMapping("/{id}")
    public ResponseEntity<Role> getOne(@PathVariable("id") Long id) {
        return new ResponseEntity<>(roleService.findById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<Role>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order
    ){
        return new ResponseEntity<>(roleService.findAll(pageNumber, pageSize, field, order), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Role> createOne(@RequestBody @Valid RoleRequest one) {
        Role roleOne = roleService.create(one);
        return new ResponseEntity<>(roleOne, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Role> updateOne(@PathVariable("id") Long id, @RequestBody @Valid RoleRequest one) {
        if (!roleService.existsById(id)) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(roleService.update(id, one), HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteOne(@PathVariable("id") Long id) {

        if (!roleService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        roleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
