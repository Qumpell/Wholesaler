package pl.matkan.wholesaler.service;

import pl.matkan.wholesaler.dto.UserDto;
import pl.matkan.wholesaler.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User one);

    User update(Long id, UserDto one);

    UserDto findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<UserDto> findAll();
}
