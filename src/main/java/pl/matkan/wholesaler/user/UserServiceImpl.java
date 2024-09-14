package pl.matkan.wholesaler.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.exception.BadRequestException;
import pl.matkan.wholesaler.exception.ResourceNotFoundException;
import pl.matkan.wholesaler.role.RoleRepository;
import pl.matkan.wholesaler.role.RoleService;
import pl.matkan.wholesaler.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    //    private final UserResponseMapper userResponseMapper;
//    private final UserRequestMapper userRequestMapper;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;


    @Override
    public UserResponse create(UserRequest dto) {

        User user = userMapper.userRequestToUser(dto);

        user = validateAndSetRole(user, dto.roleId());

        try {
            user = userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User with username:=" + dto.username() + " already exists");
        }
        return userMapper.userToUserResponse(user);

//        try {
//            roleService.findByName(one.roleName());
//        } catch (ResourceNotFoundException e) {
//            throw new BadRequestException(e.getMessage(), e.getMessage());
//        }
//
//        User userToCreate = userRequestMapper.userRequestToUser(one);
//
//        try {
//            User userSaved = userRepo.save(userToCreate);
//
//            return userResponseMapper.userToUserResponse(userSaved);
//
//        } catch (DataIntegrityViolationException e) {
//            throw new DataIntegrityViolationException("User with login:=" + one.login() + " already exists");
//        }

    }

    @Override
    public UserResponse update(Long id, UserRequest userRequest) {
        User existingUser = getOneById(id);

        existingUser = updateExistingUser(existingUser, userRequest);

        try {
            existingUser = userRepo.save(existingUser);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User with username:=" + userRequest.username() + " already exists");
        }
        return userMapper.userToUserResponse(existingUser);


//        if(!existsById(id)){
//            throw new ResourceNotFoundException("User was not found" ,"with id: " + id);
//        }
//
//        try{
//            roleService.findByName(one.roleName());
//        }catch (ResourceNotFoundException e){
//            throw new BadRequestException(e.getMessage(), e.getErrorDetails());
//        }
//
//        try {
//
//            User userToUpdate = userRequestMapper.userRequestToUser(one);
//            userToUpdate.setId(id);
//            User userSaved = userRepo.save(userToUpdate);
//
//            return userResponseMapper.userToUserResponse(userSaved);
//
//        }catch (DataIntegrityViolationException e){
//            throw new DataIntegrityViolationException("User with login:=" + one.login() + " already exists");
//        }

//        User userDataToUpdate = userResponseMapper.userDtoToUser(one);
//        User clientFetched = getOneUserById(id);

//        clientFetched.setFirstname(userDataToUpdate.getFirstname());
//        clientFetched.setSurname(userDataToUpdate.getSurname());
//        clientFetched.setLogin(userDataToUpdate.getLogin());
//        clientFetched.setDateOfBirth(userDataToUpdate.getDateOfBirth());
//        clientFetched.setRole(roleService.findByName(one.getRoleName()));

//        return userRepo.save(clientFetched);
    }


    @Override
    public UserResponse findById(Long id) {
//        return userResponseMapper.userToUserResponse(
//                userRepo
//                        .findById(id)
//                        .orElseThrow((() -> new EntityNotFoundException("User not found", "with given id:= " + id)))
//        );
        return userMapper.userToUserResponse(getOneById(id));
    }

    @Override
    public boolean existsById(Long id) {
        return userRepo.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(userMapper::userToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserResponse> findAll(int offset, int pageSize, String field, String order) {
        Page<User> users = userRepo.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field)));
        return users.map(userMapper::userToUserResponse);
    }

    public User getOneById(Long id) {
        return userRepo.findById(id)
                .orElseThrow((() -> new ResourceNotFoundException("User was not found", "with given id:= " + id))
                );
    }

    private User validateAndSetRole(User user, Long roleId) {
        try {
            user.setRole(roleService.findById(roleId));
        } catch (ResourceNotFoundException e) {
            throw new BadRequestException("Invalid payload", e.getMessage() + " " + e.getErrorDetails());
        }
        return user;
    }

    private User updateExistingUser(User existingUser, UserRequest userRequest) {

        existingUser.setFirstname(userRequest.firstname());
        existingUser.setUsername(userRequest.username());
        existingUser.setUsername(userRequest.username());
        existingUser.setPassword(userRequest.password());
        existingUser.setDateOfBirth(userRequest.dateOfBirth());

        return validateAndSetRole(existingUser, userRequest.roleId());
    }

}
