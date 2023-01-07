package pl.matkan.wholesaler.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.matkan.wholesaler.model.TradeNote;

@Repository
public interface TradeNoteRepository extends JpaRepository<TradeNote, Long> {
}
