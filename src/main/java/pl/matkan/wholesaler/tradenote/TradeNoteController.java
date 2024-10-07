package pl.matkan.wholesaler.tradenote;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/trade-notes")
public class TradeNoteController {

    private final TradeNoteService tradeNoteService;

    @GetMapping("/{id}")
    public ResponseEntity<TradeNoteResponse> getOne(@PathVariable("id") Long id) {
        TradeNoteResponse tradeNoteResponse = tradeNoteService.findById(id);
        return new ResponseEntity<>(tradeNoteResponse, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<TradeNoteResponse>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return new ResponseEntity<>(
                tradeNoteService.findAll(pageNumber, pageSize, field, order),
                HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<TradeNoteResponse> createOne(@RequestBody @Valid TradeNoteRequest one) {
        return new ResponseEntity<>(tradeNoteService.create(one), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TradeNoteResponse> updateOne(@PathVariable("id") Long id, @RequestBody @Valid TradeNoteRequest one) {
        if (tradeNoteService.existsById(id)) {
            return new ResponseEntity<>(tradeNoteService.update(id, one), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteOne(@PathVariable("id") Long id) {

        if (!tradeNoteService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        tradeNoteService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
