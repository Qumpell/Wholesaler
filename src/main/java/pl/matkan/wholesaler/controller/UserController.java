package pl.matkan.wholesaler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.matkan.wholesaler.model.User;
import pl.matkan.wholesaler.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/wholesaler/users")
public class UserController {

    public UserService userSrv;

    /* READ */
    @GetMapping("/{id}")
    public ResponseEntity<User> getOne(@PathVariable("id") Long id) {
        Optional<User> one = userSrv.findById(id);

        return one.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping()
    public List<User> getAll() {
        System.out.println("trata  " + userSrv.findAll().toString());
        return userSrv.findAll();
    }

    /* CREATE */
    @PostMapping()
    public ResponseEntity<User> createOne(@RequestBody User one) {
        System.out.println("co przyszlo:" + one.toString());
        User carOne = userSrv.create(one);
        return new ResponseEntity<>(carOne, HttpStatus.CREATED);
    }

    /* UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateOne(@PathVariable("id") Long id, @RequestBody User one) {
        if (userSrv.existsById(id)) {
            User updatedOne = userSrv.update(id, one);
            return new ResponseEntity<>(updatedOne, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /* DELETE */
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteOne(@PathVariable("id") Long id) {
        userSrv.deleteById(id);
    }
}
