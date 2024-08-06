package pl.matkan.wholesaler.user;

import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    User create(UserDto one);

    User update(Long id, UserDto one);

    UserDto findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<UserDto> findAll();

    Page<UserDto> findUsersWithPaginationAndSort(int pageNumber, int pageSize, String field, String order);
}
