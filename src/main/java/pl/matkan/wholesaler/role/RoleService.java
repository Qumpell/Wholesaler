package pl.matkan.wholesaler.role;

import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {
    Role create(RoleRequest one);

    Role update(Long id, RoleRequest one);

    Role findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<Role> findAll();

    Page<Role> findRolesWithPaginationAndSort(int pageNumber, int pageSize, String field, String order);

    Role findByName(String name);
}
