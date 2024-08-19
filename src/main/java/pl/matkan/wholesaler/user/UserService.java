package pl.matkan.wholesaler.user;

import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest one);

    UserResponse update(Long id, UserRequest one);

    UserResponse findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<UserResponse> findAll();

    Page<UserResponse> findUsersWithPaginationAndSort(int pageNumber, int pageSize, String field, String order);

    void existsByIdOrThrow(Long id);
}
