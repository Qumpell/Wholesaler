package pl.matkan.wholesaler.user;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public ResponseEntity<UserDto> getOne(@PathVariable("id") Long id) {
        UserDto userDto = userSrv.findById(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<Page<UserDto>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return new ResponseEntity<>(userSrv.findAllUsers(pageNumber, pageSize, field, order), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<User> createOne(@RequestBody UserDto one) {
        User carOne = userSrv.create(one);
        return new ResponseEntity<>(carOne, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateOne(@PathVariable("id") Long id, @RequestBody UserDto one) {
        if (userSrv.existsById(id)) {
            User updatedOne = userSrv.update(id, one);
            return new ResponseEntity<>(updatedOne, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Long> deleteOne(@PathVariable("id") Long id) {
        if (userSrv.existsById(id)) {
            userSrv.deleteById(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }
}
