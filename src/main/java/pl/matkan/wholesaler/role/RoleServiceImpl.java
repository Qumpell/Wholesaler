package pl.matkan.wholesaler.role;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.matkan.wholesaler.exception.ResourceNotFoundException;
import pl.matkan.wholesaler.user.User;
import pl.matkan.wholesaler.user.UserRepository;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Service("roleService")
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepo;
    private final UserRepository userRepository;

    @Override
    public Role create(RoleRequest one) {
        Role role = RoleRequestMapper.INSTANCE.roleRequestToRole(one);
        try {
            return roleRepo.save(role);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Role with name:=" + one.name() + " already exists");
        }

    }

    @Override
    public Role update(Long id, RoleRequest one) {
        return roleRepo.findById(id).map(role -> {

            role = RoleRequestMapper.INSTANCE.roleRequestToRole(one);

            try {

                return roleRepo.save(role);

            } catch (DataIntegrityViolationException ex) {
                throw new DataIntegrityViolationException("Role with name:=" + one.name() + " already exists");
            }

        }).orElseThrow(() -> new ResourceNotFoundException("Role was not found", "with id: " + id));

    }

    @Override
    public Role findById(Long id) {
        return roleRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found ", "with given id:= " + id));

    }

    @Override
    public boolean existsById(Long id) {
        return roleRepo.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Role role = findById(id);
        Collection<User> users = role.getUsers();

        for(User user : users) {
            user.getRoles().remove(role);
        }

        userRepository.saveAll(users);
        roleRepo.delete(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepo.findAll();
    }


    @Override
    public Page<Role> findAll(int pageNumber, int pageSize, String field, String order) {
        return roleRepo.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field))
        );
    }
}
