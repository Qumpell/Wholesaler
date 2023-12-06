package pl.matkan.wholesaler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.matkan.wholesaler.dto.TradeNoteDto;
import pl.matkan.wholesaler.model.TradeNote;
import pl.matkan.wholesaler.service.impl.TradeNoteServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tradeNotes")
public class TradeNoteController {

    private final TradeNoteServiceImpl tradeNoteService;


    public TradeNoteController(TradeNoteServiceImpl tradeNoteService) {
        this.tradeNoteService = tradeNoteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TradeNote> getOne(@PathVariable("id") Long id) {
        Optional<TradeNote> one = tradeNoteService.findById(id);
        return one.map(tradeNote -> new ResponseEntity<>(tradeNote, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping()
    public List<TradeNoteDto> getAll() {
        return tradeNoteService.findAll();
    }

    @PostMapping()
    public ResponseEntity<TradeNote> createOne(@RequestBody TradeNote one) {
        TradeNote tradeNoteOne = tradeNoteService.create(one);
        return new ResponseEntity<>(tradeNoteOne, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TradeNote> updateOne(@PathVariable("id") Long id, @RequestBody TradeNote one) {
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
