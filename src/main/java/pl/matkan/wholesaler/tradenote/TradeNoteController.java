package pl.matkan.wholesaler.tradenote;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tradeNotes")
public class TradeNoteController {

    private final TradeNoteService tradeNoteService;

    @GetMapping("/{id}")
    public ResponseEntity<TradeNoteDto> getOne(@PathVariable("id") Long id) {
        TradeNoteDto tradeNoteDto = tradeNoteService.findById(id);
        return new ResponseEntity<>(tradeNoteDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<TradeNoteDto>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return new ResponseEntity<>(
                tradeNoteService.findTradeNotesWithPaginationAndSort(pageNumber, pageSize, field, order),
                HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<TradeNote> createOne(@RequestBody TradeNoteDto one) {
        TradeNote tradeNoteOne = tradeNoteService.create(one);
        return new ResponseEntity<>(tradeNoteOne, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TradeNote> updateOne(@PathVariable("id") Long id, @RequestBody TradeNoteDto one) {
        if (tradeNoteService.existsById(id)) {
            TradeNote updatedOne = tradeNoteService.update(id, one);
            return new ResponseEntity<>(updatedOne, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Long> deleteOne(@PathVariable("id") Long id) {
        if (tradeNoteService.existsById(id)) {
            tradeNoteService.deleteById(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

}
