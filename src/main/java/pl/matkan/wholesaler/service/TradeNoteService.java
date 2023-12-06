package pl.matkan.wholesaler.service;

import pl.matkan.wholesaler.dto.TradeNoteDto;
import pl.matkan.wholesaler.model.TradeNote;

import java.util.List;
import java.util.Optional;

public interface TradeNoteService {
    public TradeNote create(TradeNote one);

    public TradeNote update(Long id, TradeNote one);

    Optional<TradeNote> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    public List<TradeNoteDto> findAll();
}
