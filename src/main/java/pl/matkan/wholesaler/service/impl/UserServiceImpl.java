package pl.matkan.wholesaler.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.dto.UserDto;
import pl.matkan.wholesaler.dto.mapper.UserMapper;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.model.User;
import pl.matkan.wholesaler.repo.UserRepository;
import pl.matkan.wholesaler.service.RoleService;
import pl.matkan.wholesaler.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepo, UserMapper userMapper, RoleService roleService) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.roleService = roleService;
    }

    @Override
    public User create(UserDto one) {
        User userToCreate = userMapper.userDtoToUser(one);
        userToCreate.setRole(roleService.findByName(one.getRoleName()));

        return userRepo.save(userToCreate);
    }

    @Override
    public User update(Long id, UserDto one) {
        User userDataToUpdate = userMapper.userDtoToUser(one);
        User clientFetched = getOneUserById(id);

        clientFetched.setName(userDataToUpdate.getName());
        clientFetched.setSurname(userDataToUpdate.getSurname());
        clientFetched.setLogin(userDataToUpdate.getLogin());
        clientFetched.setDateOfBirth(userDataToUpdate.getDateOfBirth());
        clientFetched.setRole(roleService.findByName(one.getRoleName()));

        return userRepo.save(clientFetched);
    }

    @Override
    public UserDto findById(Long id) {
        return userMapper.userToUserDto(
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
    public List<UserDto> findAll() {
        List<User> users = userRepo.findAll();
        return users.stream()
                                        .map(userMapper::userToUserDto)
                                        .collect(Collectors.toList());
    }

    public Page<UserDto> findAllUsers(int offset, int pageSize, String field, String order) {
        Page<User> users  = userRepo.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.fromString(order), field)));
        return users.map(userMapper::userToUserDto);
    }

    public User getOneUserById(Long id) {
        return userRepo.findById(id)
                             .orElseThrow((() -> new EntityNotFoundException("User not found", "with given id:= " + id))
        );
    }
}
