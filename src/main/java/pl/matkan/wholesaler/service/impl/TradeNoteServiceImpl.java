package pl.matkan.wholesaler.service.impl;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.model.TradeNote;
import pl.matkan.wholesaler.repo.TradeNoteRepository;
import pl.matkan.wholesaler.service.TradeNoteService;

import java.util.List;
import java.util.Optional;

@Service("tradeNoteService")
@Repository
public class TradeNoteServiceImpl implements TradeNoteService {

    private final TradeNoteRepository tradeNoteRepo;

    public TradeNoteServiceImpl(TradeNoteRepository tradeNoteRepo) {
        this.tradeNoteRepo = tradeNoteRepo;
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
    public List<TradeNote> findAll() {
        return tradeNoteRepo.findAll();
    }
}
