package pl.matkan.wholesaler.controller;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.matkan.wholesaler.dto.UserDto;
import pl.matkan.wholesaler.model.User;
import pl.matkan.wholesaler.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserServiceImpl userSrv;

    public UserController(@Qualifier("userService") UserServiceImpl userSrv) {
        this.userSrv = userSrv;
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getOne(@PathVariable("id") Long id) {
        Optional<User> one = userSrv.findById(id);
        return one.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
    @GetMapping()
    public List<UserDto> getAll() {
        return userSrv.findAll();
    }

    @PostMapping()
    public ResponseEntity<User> createOne(@RequestBody User one) {
        User carOne = userSrv.create(one);
        return new ResponseEntity<>(carOne, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateOne(@PathVariable("id") Long id, @RequestBody User one) {
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
