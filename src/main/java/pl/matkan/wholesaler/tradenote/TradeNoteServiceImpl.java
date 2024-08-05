package pl.matkan.wholesaler.tradenote;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.company.CompanyServiceImpl;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.user.UserServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service("tradeNoteService")
@RequiredArgsConstructor
public class TradeNoteServiceImpl implements TradeNoteService {

    private final TradeNoteRepository tradeNoteRepo;
    private final TradeNoteMapper tradeNoteMapper;
    private final CompanyServiceImpl companyService;
    private final UserServiceImpl userService;

    @Override
    public TradeNote create(TradeNoteDto one) {
        TradeNote tradeNoteToCreate = tradeNoteMapper.tradeNoteDtoToTradeNote(one);
        tradeNoteToCreate.setCompany(companyService.getOneCompanyById(one.getCompanyId()));
        tradeNoteToCreate.setUser(userService.getOneUserById(one.getOwnerId()));

        return tradeNoteRepo.save(tradeNoteToCreate);
    }

    @Override
    public TradeNote update(Long id, TradeNoteDto one) {
        TradeNote tradeNoteUpdated = tradeNoteMapper.tradeNoteDtoToTradeNote(one);

        tradeNoteUpdated.setId(id);
        tradeNoteUpdated.setUser(userService.getOneUserById(one.getOwnerId()));
        tradeNoteUpdated.setCompany(companyService.getOneCompanyById(one.getCompanyId()));

        return tradeNoteRepo.save(tradeNoteUpdated);
    }

    @Override
    public TradeNoteDto findById(Long id) {
        return tradeNoteMapper.tradeNoteToTradeNoteDto(
                tradeNoteRepo.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Trade note was not found", "with given id:= " + id))
        );
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

    @Override
    public Page<TradeNoteDto> findTradeNotesWithPaginationAndSort(
            int pageNumber, int pageSize, String field, String order)
    {
        Page<TradeNote> tradeNotes = tradeNoteRepo.findAll(PageRequest.of(pageNumber, pageSize).
                withSort(Sort.by(Sort.Direction.fromString(order), field))
        );
        return tradeNotes.map(tradeNoteMapper::tradeNoteToTradeNoteDto);
    }
}
