package pl.matkan.wholesaler.tradenote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeNoteRepository extends JpaRepository<TradeNote, Long> {

//    @Modifying
//    @Query("UPDATE TradeNote tn SET tn.companyName = :newCompanyName WHERE tn.companyName = :oldCompanyName")
//    int updateCompanyNameInTradeNotes(@Param("oldCompanyName") String oldCompanyName, @Param("newCompanyName") String newCompanyName);

    @Query("SELECT new pl.matkan.wholesaler.tradenote.TradeNoteResponse(t.id, t.content, c.name, CONCAT(u.firstname, ' ', u.surname), t.companyId, t.ownerId) " +
            "FROM TradeNote t " +
            "JOIN Company c ON t.companyId = c.id " +
            "JOIN User u ON t.ownerId = u.id")
    Page<TradeNoteResponse> findAllTradeNotesWithCompanyNameAndOwnerName(Pageable pageable);

    @Query("SELECT new pl.matkan.wholesaler.tradenote.TradeNoteResponse(t.id, t.content, c.name, CONCAT(u.firstname, ' ', u.surname), t.companyId, t.ownerId) " +
            "FROM TradeNote t " +
            "JOIN Company c ON t.companyId = c.id " +
            "JOIN User u ON t.ownerId = u.id " +
            "WHERE t.id = :id")
    TradeNoteResponse findTradeNoteByIdWithCompanyNameAndOwnerName(@Param("id") Long id);
}

