package pl.matkan.wholesaler.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.matkan.wholesaler.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
