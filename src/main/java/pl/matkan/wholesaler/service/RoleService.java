package pl.matkan.wholesaler.service;

import pl.matkan.wholesaler.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    public Role create(Role one);

    public Role update(Long id, Role one);

    Optional<Role> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    public List<Role> findAll();
}
