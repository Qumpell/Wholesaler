package pl.matkan.wholesaler.tradenote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeNoteRepository extends JpaRepository<TradeNote, Long> {
}
