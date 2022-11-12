package pl.matkan.wholesaler.service;

import pl.matkan.wholesaler.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User one);

    User update(Long id, User one);

    Optional<User> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<User> findAll();
}
