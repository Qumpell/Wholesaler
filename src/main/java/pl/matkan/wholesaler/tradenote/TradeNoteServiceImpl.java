package pl.matkan.wholesaler.tradenote;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.company.CompanyService;
import pl.matkan.wholesaler.exception.BadRequestException;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.tradenote.mapper.TradeNoteRequestMapper;
import pl.matkan.wholesaler.tradenote.mapper.TradeNoteResponseMapper;
import pl.matkan.wholesaler.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service("tradeNoteService")
@RequiredArgsConstructor
public class TradeNoteServiceImpl implements TradeNoteService {

    private final TradeNoteRepository tradeNoteRepo;
    private final TradeNoteResponseMapper tradeNoteResponseMapper;
    private final TradeNoteRequestMapper tradeNoteRequestMapper;
    private final CompanyService companyService;
    private final UserService userService;

    @Override
    public TradeNoteResponse create(TradeNoteRequest one) {
        final String companyName;

        try {
            companyName = companyService.findById(one.companyId()).getName();
//            companyService.existsByNameOrThrow(one.companyName());
            userService.existsByIdOrThrow(one.ownerId());

        }catch (EntityNotFoundException e) {
            throw new BadRequestException("Invalid payload", e.getMessage() + " " + e.getErrorDetails());
        }

        TradeNote tradeNoteToCreate = tradeNoteRequestMapper.tradeNoteRequestToTradeNote(one);

        TradeNote tradeNote = tradeNoteRepo.save(tradeNoteToCreate);

        return tradeNoteResponseMapper.tradeNoteToTradeNoteResponse(tradeNote, companyName);
    }

    @Override
    public TradeNoteResponse update(Long id, TradeNoteRequest one) {

        TradeNote existingTradeNote = findById(id);

        try {
            companyService.existsByNameOrThrow(one.companyName());
//            companyService.existsById(one.companyId());
            userService.existsByIdOrThrow(one.ownerId());

        }catch (EntityNotFoundException e) {
            throw new BadRequestException("Invalid payload", e.getMessage() + " " +  e.getErrorDetails());
        }

        tradeNoteRequestMapper.updateTradeNoteFromRequest(one, existingTradeNote);

        TradeNote tradeNote = tradeNoteRepo.save(existingTradeNote);

        return tradeNoteResponseMapper.tradeNoteToTradeNoteResponse(tradeNote);
    }

    @Override
    public TradeNoteResponse findById(Long id) {
        return tradeNoteResponseMapper.tradeNoteToTradeNoteResponse(
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
    public List<TradeNoteResponse> findAll() {
        List<TradeNote> tradeNotes = tradeNoteRepo.findAll();
        return tradeNotes.stream()
                .map(tradeNoteResponseMapper::tradeNoteToTradeNoteResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TradeNoteResponse> findTradeNotesWithPaginationAndSort(
            int pageNumber, int pageSize, String field, String order)
    {
        Page<TradeNote> tradeNotes = tradeNoteRepo.findAll(PageRequest.of(pageNumber, pageSize).
                withSort(Sort.by(Sort.Direction.fromString(order), field))
        );
        return tradeNotes.map(tradeNoteResponseMapper::tradeNoteToTradeNoteResponse);
    }

    private void validateOwnerAndCompany(Long ownerId, Long companyId) {

    }
}
