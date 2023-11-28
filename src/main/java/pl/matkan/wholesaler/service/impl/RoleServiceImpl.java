package pl.matkan.wholesaler.service.impl;

import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.model.Role;
import pl.matkan.wholesaler.model.User;
import pl.matkan.wholesaler.repo.RoleRepository;
import pl.matkan.wholesaler.repo.UserRepository;
import pl.matkan.wholesaler.service.RoleService;

import java.util.List;
import java.util.Optional;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepo;
    private final UserRepository userRepository;

    public RoleServiceImpl(RoleRepository roleRepo, UserRepository userRepository) {
        this.roleRepo = roleRepo;
        this.userRepository = userRepository;
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
    public Optional<Role> findById(Long id) {
        return roleRepo.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return roleRepo.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Role> roleOptional = findById(id);
        if (roleOptional.isPresent()){
            Role role = roleOptional.get();
            List<User> users = role.getUsers();
            if(users != null){
                for(User user : users){
//                    role.removeUser(user);
                    user.setRole(null);
                }
                userRepository.saveAll(users);
            }
        }
        roleRepo.deleteById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleRepo.findAll();
    }
}
