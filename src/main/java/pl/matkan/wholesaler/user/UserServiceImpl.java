package pl.matkan.wholesaler.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.exception.BadRequestException;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.role.RoleService;
import pl.matkan.wholesaler.user.mapper.UserRequestMapper;
import pl.matkan.wholesaler.user.mapper.UserResponseMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserResponseMapper userResponseMapper;
    private final UserRequestMapper userRequestMapper;
    private final RoleService roleService;


    @Override
    public UserResponse create(UserRequest one) {

        try {
            roleService.findByName(one.roleName());
        } catch (EntityNotFoundException e) {
            throw new BadRequestException(e.getMessage(), e.getMessage());
        }

        User userToCreate = userRequestMapper.userRequestToUser(one);

        try {
            User userSaved = userRepo.save(userToCreate);

            return userResponseMapper.userToUserResponse(userSaved);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User with login:=" + one.login() + " already exists");
        }

    }

    @Override
    public UserResponse update(Long id, UserRequest one) {


        if(!existsById(id)){
            throw new EntityNotFoundException("User was not found" ,"with id: " + id);
        }

        try{
            roleService.findByName(one.roleName());
        }catch (EntityNotFoundException e){
            throw new BadRequestException(e.getMessage(), e.getErrorDetails());
        }

        try {

            User userToUpdate = userRequestMapper.userRequestToUser(one);
            userToUpdate.setId(id);
            User userSaved = userRepo.save(userToUpdate);

            return userResponseMapper.userToUserResponse(userSaved);

        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("User with login:=" + one.login() + " already exists");
        }

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
        return userResponseMapper.userToUserResponse(
                userRepo
                        .findById(id)
                        .orElseThrow((() -> new EntityNotFoundException("User not found", "with given id:= " + id)))
        );
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
                .map(userResponseMapper::userToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserResponse> findUsersWithPaginationAndSort(int offset, int pageSize, String field, String order) {
        Page<User> users = userRepo.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field)));
        return users.map(userResponseMapper::userToUserResponse);
    }

    @Override
    public void existsByIdOrThrow(Long id) {
        if (!userRepo.existsById(id)) {
            throw new EntityNotFoundException("User not found", "with given id:= " + id);
        }
    }

//    public User getOneUserById(Long id) {
//        return userRepo.findById(id)
//                             .orElseThrow((() -> new EntityNotFoundException("User not found", "with given id:= " + id))
//        );
//    }
}
