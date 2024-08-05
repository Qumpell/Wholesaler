package pl.matkan.wholesaler.tradenote;

import org.springframework.data.domain.Page;

import java.util.List;

public interface TradeNoteService {
    TradeNote create(TradeNoteDto one);

    TradeNote update(Long id, TradeNoteDto one);

    TradeNoteDto findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<TradeNoteDto> findAll();

    Page<TradeNoteDto> findTradeNotesWithPaginationAndSort(int pageNumber, int pageSize, String field, String order);
}
