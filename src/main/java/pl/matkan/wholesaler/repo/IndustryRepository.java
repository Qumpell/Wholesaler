package pl.matkan.wholesaler.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.matkan.wholesaler.model.Industry;

import javax.persistence.criteria.CriteriaBuilder;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, Long> {
}
