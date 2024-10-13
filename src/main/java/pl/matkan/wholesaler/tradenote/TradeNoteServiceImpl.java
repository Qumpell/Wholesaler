package pl.matkan.wholesaler.tradenote;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.company.CompanyService;
import pl.matkan.wholesaler.exception.BadRequestException;
import pl.matkan.wholesaler.exception.ResourceNotFoundException;
import pl.matkan.wholesaler.tradenote.mapper.TradeNoteMapper;
import pl.matkan.wholesaler.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service("tradeNoteService")
@RequiredArgsConstructor
public class TradeNoteServiceImpl implements TradeNoteService {

    private final TradeNoteRepository tradeNoteRepo;
    private final TradeNoteMapper tradeNoteMapper;
    private final CompanyService companyService;
    private final UserService userService;

    @Override
    public TradeNoteResponse create(TradeNoteDetailedRequest dto) {

        TradeNote tradeNote = tradeNoteMapper.tradeNoteRequestToTradeNote(dto);

        tradeNote = tradeNoteRepo.save(validateAndSetOwnerAndCompany(tradeNote, dto.ownerId(), dto.companyId()));


        return tradeNoteMapper.tradeNoteToTradeNoteResponse(tradeNote);
    }

    @Override
    public TradeNoteResponse update(Long id, TradeNoteDetailedRequest dto) {

        TradeNote existingTradeNote = getOneById(id);

        existingTradeNote = updateExisitingTradeNote(existingTradeNote, dto);

        return tradeNoteMapper.tradeNoteToTradeNoteResponse(tradeNoteRepo.save(existingTradeNote));
    }

    @Override
    public TradeNoteResponse findById(Long id) {
        return tradeNoteMapper.tradeNoteToTradeNoteResponse(getOneById(id));
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
                .map(tradeNoteMapper::tradeNoteToTradeNoteResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TradeNoteResponse> findAll(
            int pageNumber, int pageSize, String field, String order)
    {
        Page<TradeNote> tradeNotes = tradeNoteRepo.findAll(PageRequest.of(pageNumber, pageSize).
                withSort(Sort.by(Sort.Direction.fromString(order), field))
        );
        return tradeNotes.map(tradeNoteMapper::tradeNoteToTradeNoteResponse);
    }

    private TradeNote validateAndSetOwnerAndCompany(TradeNote tradeNote, Long ownerId, Long companyId) {

        try {
            tradeNote.setUser(userService.getOneById(ownerId));
            tradeNote.setCompany(companyService.getOneById(companyId));
        }
        catch (ResourceNotFoundException e){
            throw new BadRequestException("Invalid payload", e.getMessage() + " " +  e.getErrorDetails());
        }

        return tradeNote;
    }

    private TradeNote getOneById(Long id) {
        return tradeNoteRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trade note was not found", "with given id:= " + id));
    }

    private TradeNote updateExisitingTradeNote(TradeNote existingTradeNote, TradeNoteDetailedRequest dto) {
        existingTradeNote.setContent(dto.content());

        return validateAndSetOwnerAndCompany(existingTradeNote, dto.ownerId(), dto.companyId());
    }
}
