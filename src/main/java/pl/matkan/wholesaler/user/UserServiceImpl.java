package pl.matkan.wholesaler.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.matkan.wholesaler.exception.BadRequestException;
import pl.matkan.wholesaler.exception.ResourceNotFoundException;
import pl.matkan.wholesaler.role.Role;
import pl.matkan.wholesaler.role.RoleRepository;
import pl.matkan.wholesaler.role.RoleService;
import pl.matkan.wholesaler.user.mapper.UserMapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleService roleService;
    private final RoleRepository roleRepository;


    @Override
    @Transactional
    public UserResponse create(UserRequest dto) {

        User user = UserMapper.INSTANCE.userRequestToUser(dto);

        try {
            user = userRepo.save(validateAndSetRoles(user, dto.roleIds()));
            attachUsersToRoles(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User with username:=" + dto.username() + " already exists");
        }
        return UserMapper.INSTANCE.userToUserResponse(user);

    }

    private void attachUsersToRoles(User user) {
        for(Role role : user.getRoles()){
            role.getUsers().add(user);
        }
        roleRepository.saveAll(user.getRoles());
    }

    @Override
    public UserResponse update(Long id, UserRequest userRequest) {
        User existingUser = getOneById(id);

        existingUser = updateExistingUser(existingUser, userRequest);

        try {
            existingUser = userRepo.save(existingUser);
            attachUsersToRoles(existingUser);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User with username:=" + userRequest.username() + " already exists");
        }
        return UserMapper.INSTANCE.userToUserResponse(existingUser);

    }


    @Override
    public UserResponse findById(Long id) {
        return UserMapper.INSTANCE.userToUserResponse(getOneById(id));
    }

    @Override
    public boolean existsById(Long id) {
        return userRepo.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = getOneById(id);
        Collection<Role> roles = user.getRoles();
        for(Role role : roles){
            role.getUsers().remove(user);
        }
        roleRepository.saveAll(roles);
        userRepo.delete(user);
    }

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(UserMapper.INSTANCE::userToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserResponse> findAll(int offset, int pageSize, String field, String order) {
        Page<User> users = userRepo.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field)));
        return users.map(UserMapper.INSTANCE::userToUserResponse);
    }

    public User getOneById(Long id) {
        return userRepo.findById(id)
                .orElseThrow((() -> new ResourceNotFoundException("User was not found", "with given id:= " + id))
                );
    }

    private User validateAndSetRoles(User user, Set<Long> roleIds) {

        Set<Role> roles = new HashSet<>();
        for (Long roleId : roleIds) {
            try {
                Role role = roleService.findById(roleId);
                roles.add(role);
            }catch (ResourceNotFoundException e){
                throw new BadRequestException("Invalid payload", e.getMessage() + " " + e.getErrorDetails());
            }
        }

        user.setRoles(roles);

        return user;
    }

    private User updateExistingUser(User existingUser, UserRequest userRequest) {

        existingUser.setFirstname(userRequest.firstname());
        existingUser.setUsername(userRequest.username());
        existingUser.setUsername(userRequest.username());
        existingUser.setPassword(userRequest.password());
        existingUser.setDateOfBirth(userRequest.dateOfBirth());

        return validateAndSetRoles(existingUser, userRequest.roleIds());
    }

}
