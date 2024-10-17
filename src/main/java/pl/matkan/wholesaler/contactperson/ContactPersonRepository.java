package pl.matkan.wholesaler.contactperson;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
    Page<ContactPerson> findByUserIdAndIsDeletedFalse(PageRequest pageable, Long userId);
}
