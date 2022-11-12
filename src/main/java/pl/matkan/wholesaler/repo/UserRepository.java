package pl.matkan.wholesaler.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.matkan.wholesaler.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
