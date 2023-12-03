package pl.matkan.wholesaler.service.impl;

import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.dto.UserDto;
import pl.matkan.wholesaler.dto.mapper.UserMapper;
import pl.matkan.wholesaler.model.User;
import pl.matkan.wholesaler.repo.UserRepository;
import pl.matkan.wholesaler.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    @Override
    public User create(User one) {
        return userRepo.save(one);
    }

    @Override
    public User update(Long id, User one) {
        one.setId(id);
        return userRepo.save(one);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
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
        List<UserDto> userDtos = users.stream()
                                        .map(userMapper::userToUserDto)
                                        .collect(Collectors.toList());
        return userDtos;
    }
}
