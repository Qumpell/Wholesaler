package pl.matkan.wholesaler.role;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.exception.ResourceNotFoundException;

import java.util.List;

@Service("roleService")
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepo;
//    private final UserRepository userRepository;
    private final RoleRequestMapper roleRequestMapper;

    @Override
    public Role create(RoleRequest one) {
        Role role = roleRequestMapper.roleRequestToRole(one);
        try {
            return roleRepo.save(role);
        }catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Role with name:=" + one.name() + " already exists");
        }

    }

    @Override
    public Role update(Long id, RoleRequest one) {
        return roleRepo.findById(id).map(role ->{

            role = roleRequestMapper.roleRequestToRole(one);

            try{

                return roleRepo.save(role);

            }catch (DataIntegrityViolationException ex) {
                throw new DataIntegrityViolationException("Role with name:=" + one.name() + " already exists");
            }

        }).orElseThrow(() -> new ResourceNotFoundException("Role was not found" ,"with id: " + id));

//        return findById(id);

        //        try{
//            one.setId(id);
//            return roleRepo.save(one);
//        }
//        catch (DataIntegrityViolationException ex) {
//            throw new DataIntegrityViolationException("Role with name:=" + one.getName() + " already exists");
//        }
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
    public void deleteById(Long id) {
//        Optional<Role> roleOptional = roleRepo.findById(id);
//        if (roleOptional.isPresent()){
//            Role role = roleOptional.get();
//            List<User> users = role.getUsers();
//            if(users != null){
//                for(User user : users){
//                    user.setRole(null);
//                }
//                userRepository.saveAll(users);
//            }
//        }
        roleRepo.deleteById(id);
    }

    @Override
    public List<Role> findAll() {
//       List<Role> roles = roleRepo.findAll();
//       return roles.stream()
//               .map(roleRequestMapper::roleToRoleDto)
//               .collect(Collectors.toList());
        return roleRepo.findAll();
    }

//    @Override
//    public Role findByName(String name) {
//        return roleRepo.findByName(name)
//                        .orElseThrow(
//                                () -> new EntityNotFoundException("Role not found ", "with given name:= " + name)
//                        );
//    }

    @Override
    public Page<Role> findAll(int pageNumber, int pageSize, String field, String order) {
        return roleRepo.findAll(
                PageRequest.of(pageNumber, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field))
        );
    }
}
