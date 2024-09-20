package pl.matkan.wholesaler.tradenote;

import org.springframework.data.domain.Page;

import java.util.List;

public interface TradeNoteService {
    TradeNoteResponse create(TradeNoteRequest one);

    TradeNoteResponse update(Long id, TradeNoteRequest one);

    TradeNoteResponse findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<TradeNoteResponse> findAll();
    Page<TradeNoteResponse> findAll(int pageNumber, int pageSize, String field, String order);
}
