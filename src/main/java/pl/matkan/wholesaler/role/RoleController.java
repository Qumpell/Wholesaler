package pl.matkan.wholesaler.role;

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
    public ResponseEntity<RoleDto> getOne(@PathVariable("id") Long id) {
        RoleDto roleDto = roleService.findById(id);
        return new ResponseEntity<>(roleDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<RoleDto>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order
    ){
        return new ResponseEntity<>(roleService.findAll(pageNumber, pageSize, field, order), HttpStatus.OK);
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

//    @GetMapping("/user-with-role/{userId}")
//    public ResponseEntity<UserWithRoleDto> getUserWithRole(@PathVariable Long userId) {
//        UserDto user = userService.findById(userId);
//        RoleDto role = roleService.findRoleForUser(userId);
//        return new ResponseEntity<>(new UserWithRoleDto(user, role), HttpStatus.OK);
//    }

}
