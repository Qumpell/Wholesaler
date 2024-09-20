package pl.matkan.wholesaler.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userSrv;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getOne(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userSrv.findById(id), HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<Page<UserResponse>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return new ResponseEntity<>(userSrv.findAll(pageNumber, pageSize, field, order),
                HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<UserResponse> createOne(@RequestBody @Valid UserRequest one) {
        return new ResponseEntity<>(userSrv.create(one), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateOne(@PathVariable("id") Long id, @RequestBody @Valid UserRequest one) {
        if (!userSrv.existsById(id)) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userSrv.update(id, one), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteOne(@PathVariable("id") Long id) {

        if (!userSrv.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userSrv.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
