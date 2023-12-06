package pl.matkan.wholesaler.service.impl;

import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.dto.TradeNoteDto;
import pl.matkan.wholesaler.dto.mapper.TradeNoteMapper;
import pl.matkan.wholesaler.model.TradeNote;
import pl.matkan.wholesaler.repo.TradeNoteRepository;
import pl.matkan.wholesaler.service.TradeNoteService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("tradeNoteService")
public class TradeNoteServiceImpl implements TradeNoteService {

    private final TradeNoteRepository tradeNoteRepo;
    private final TradeNoteMapper tradeNoteMapper;
    public TradeNoteServiceImpl(TradeNoteRepository tradeNoteRepo, TradeNoteMapper tradeNoteMapper) {
        this.tradeNoteRepo = tradeNoteRepo;
        this.tradeNoteMapper = tradeNoteMapper;
    }

    @Override
    public TradeNote create(TradeNote one) {
        return tradeNoteRepo.save(one);
    }

    @Override
    public TradeNote update(Long id, TradeNote one) {
        one.setId(id);
        return tradeNoteRepo.save(one);
    }

    @Override
    public Optional<TradeNote> findById(Long id) {
        return tradeNoteRepo.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return tradeNoteRepo.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        tradeNoteRepo.deleteById(id);
    }

    @Override
    public List<TradeNoteDto> findAll() {
        List<TradeNote> tradeNotes = tradeNoteRepo.findAll();
        return tradeNotes.stream()
                .map(tradeNoteMapper::tradeNoteToTradeNoteDto)
                .collect(Collectors.toList());
    }
}
