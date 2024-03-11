package pl.matkan.wholesaler.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.dto.RoleDto;
import pl.matkan.wholesaler.dto.mapper.RoleMapper;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.model.Role;
import pl.matkan.wholesaler.model.User;
import pl.matkan.wholesaler.repo.RoleRepository;
import pl.matkan.wholesaler.repo.UserRepository;
import pl.matkan.wholesaler.service.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepo;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepo, UserRepository userRepository, RoleMapper roleMapper) {
        this.roleRepo = roleRepo;
        this.userRepository = userRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public Role create(Role one) {
        return roleRepo.save(one);
    }

    @Override
    public Role update(Long id, Role one) {
        one.setId(id);
        return roleRepo.save(one);
    }

    @Override
    public RoleDto findById(Long id) {
        return roleMapper.roleToRoleDto(
                roleRepo.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Role not found ", "with given id:= " + id))
        );
    }

    @Override
    public boolean existsById(Long id) {
        return roleRepo.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Role> roleOptional = roleRepo.findById(id);
        if (roleOptional.isPresent()){
            Role role = roleOptional.get();
            List<User> users = role.getUsers();
            if(users != null){
                for(User user : users){
                    user.setRole(null);
                }
                userRepository.saveAll(users);
            }
        }
        roleRepo.deleteById(id);
    }

    @Override
    public List<RoleDto> findAll() {
       List<Role> roles = roleRepo.findAll();
       return roles.stream()
               .map(roleMapper::roleToRoleDto)
               .collect(Collectors.toList());
    }

    @Override
    public Role findByName(String name) {
        return roleRepo.findByName(name)
                        .orElseThrow(
                                () -> new EntityNotFoundException("Role not found ", "with given name:= " + name)
                        );
    }

    @Override
    public Page<RoleDto> findAll(int pageNumber, int pageSize, String field, String order) {
        Page<Role> roles = roleRepo.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field))
        );
        return roles.map(roleMapper::roleToRoleDto);
    }
}
