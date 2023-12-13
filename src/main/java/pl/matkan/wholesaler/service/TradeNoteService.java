package pl.matkan.wholesaler.service;

import pl.matkan.wholesaler.dto.TradeNoteDto;
import pl.matkan.wholesaler.model.TradeNote;

import java.util.List;

public interface TradeNoteService {
    TradeNote create(TradeNoteDto one);

    TradeNote update(Long id, TradeNoteDto one);

    TradeNoteDto findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    List<TradeNoteDto> findAll();
}
