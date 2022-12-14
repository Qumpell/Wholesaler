package pl.matkan.wholesaler.service.impl;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.model.Role;
import pl.matkan.wholesaler.repo.RoleRepository;
import pl.matkan.wholesaler.service.RoleService;

import java.util.List;
import java.util.Optional;

@Service("roleService")
@Repository
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepo;

    public RoleServiceImpl(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Role create(Role one) {
        Role savedOne = roleRepo.save(one);
        return savedOne;
    }

    @Override
    public Role update(Long id, Role one) {

        one.setId(id);
        Role savedOne = roleRepo.save(one);
        return savedOne;
    }

    @Override
    public Optional<Role> findById(Long id) {
        Optional<Role> one = roleRepo.findById(id);
        if (one.isPresent()) {
            return one;
        } else
            return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return roleRepo.existsById(id);
    }

    @Override
    public void deleteById(Long id) {

        roleRepo.deleteById(id);

    }

    @Override
    public List<Role> findAll() {
        List<Role> all = roleRepo.findAll();
        return all;
    }
}
