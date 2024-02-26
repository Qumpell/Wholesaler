package pl.matkan.wholesaler.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.matkan.wholesaler.dto.RoleDto;
import pl.matkan.wholesaler.model.Role;
import pl.matkan.wholesaler.service.impl.RoleServiceImpl;

@RestController
@RequestMapping(value = "/roles")
public class RoleController {
    private final RoleServiceImpl roleService;
    public RoleController(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getOne(@PathVariable("id") Long id) {
        RoleDto roleDto = roleService.findById(id);
        return new ResponseEntity<>(roleDto, HttpStatus.OK);
    }
//    @GetMapping()
//    public List<RoleDto> getAll() {
//        return roleService.findAll();
//    }
    @GetMapping()
    public ResponseEntity<Page<RoleDto>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order
    ){
        return new ResponseEntity<>(roleService.findRolesWithPaginationAndSort(pageNumber, pageSize, field, order), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Role> createOne(@RequestBody Role one) {
        Role roleOne = roleService.create(one);
        return new ResponseEntity<>(roleOne, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateOne(@PathVariable("id") Long id, @RequestBody Role one) {
        if (roleService.existsById(id)) {
            Role updatedOne = roleService.update(id, one);
            return new ResponseEntity<>(updatedOne, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

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
