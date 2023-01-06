package pl.matkan.wholesaler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.matkan.wholesaler.model.Role;
import pl.matkan.wholesaler.service.RoleService;
import pl.matkan.wholesaler.service.impl.RoleServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/roles")
public class RoleController {

    private final RoleServiceImpl roleService;

    public RoleController(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    /* READ */
    @GetMapping("/{id}")
    public ResponseEntity<Role> getOne(@PathVariable("id") Long id) {
        Optional<Role> one = roleService.findById(id);
        return one.map(role -> new ResponseEntity<>(role, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping()
    public List<Role> getAll() {
        return roleService.findAll();
    }

    /* CREATE */
    @PostMapping()
    public ResponseEntity<Role> createOne(@RequestBody Role one) {
        //System.out.println("co przyszlo:" + one.toString());
        Role roleOne = roleService.create(one);
        return new ResponseEntity<>(roleOne, HttpStatus.CREATED);
    }

    /* UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateOne(@PathVariable("id") Long id, @RequestBody Role one) {
        if (roleService.existsById(id)) {
            Role updatedOne = roleService.update(id, one);
            return new ResponseEntity<>(updatedOne, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /* DELETE */
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Long> deleteOne(@PathVariable("id") Long id) {
        if (roleService.existsById(id)) {
            roleService.deleteById(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

}
