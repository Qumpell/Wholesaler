package pl.matkan.wholesaler.service;

import pl.matkan.wholesaler.dto.RoleDto;
import pl.matkan.wholesaler.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    public Role create(Role one);

    public Role update(Long id, Role one);

    RoleDto findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    public List<RoleDto> findAll();
}
