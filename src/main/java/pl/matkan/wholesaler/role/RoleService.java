package pl.matkan.wholesaler.role;

import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {
    Role create(Role one);

    Role update(Long id, Role one);

    RoleDto findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<RoleDto> findAll();

    Page<RoleDto> findRolesWithPaginationAndSort(int pageNumber, int pageSize, String field, String order);

    Role findByName(String name);
}
