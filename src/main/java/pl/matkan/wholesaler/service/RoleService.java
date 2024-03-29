package pl.matkan.wholesaler.service;

import org.springframework.data.domain.Page;
import pl.matkan.wholesaler.dto.RoleDto;
import pl.matkan.wholesaler.model.Role;

import java.util.List;

public interface RoleService {
    Role create(Role one);

    Role update(Long id, Role one);

    RoleDto findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<RoleDto> findAll();
    Page<RoleDto> findAll(int pageNumber, int pageSize, String field, String order);

    Role findByName(String name);
}
