package pl.matkan.wholesaler.tradenote;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.matkan.wholesaler.auth.AccessControlService;
import pl.matkan.wholesaler.auth.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/trade-notes")
public class TradeNoteController {

    private final TradeNoteService tradeNoteService;
    private final AccessControlService accessControlService;

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

    @GetMapping("/{userId}/all")
    public ResponseEntity<Page<TradeNoteResponse>> getAllByUser(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String field,
            @RequestParam(defaultValue = "asc") String order,
            @PathVariable Long userId) {

        return new ResponseEntity<>(
                tradeNoteService.findAllByUser(pageNumber, pageSize, field, order, userId),
                HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<TradeNoteResponse> createOne(
            @RequestBody @Valid TradeNoteRequest one,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        TradeNoteDetailedRequest tradeNoteDetailedRequest = new TradeNoteDetailedRequest(one.content(), one.companyId(), userDetails.getId());

        return new ResponseEntity<>(tradeNoteService.create(tradeNoteDetailedRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TradeNoteResponse> updateOne(
            @PathVariable("id") Long id,
            @RequestBody @Valid TradeNoteRequest one,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        final Long ownerId = tradeNoteService.findById(id).ownerId();
        accessControlService.canAccessResource(ownerId, userDetails);
        TradeNoteDetailedRequest tradeNoteDetailedRequest = new TradeNoteDetailedRequest(one.content(), one.companyId(), ownerId);
        return new ResponseEntity<>(tradeNoteService.update(id, tradeNoteDetailedRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        accessControlService.canAccessResource(tradeNoteService.findById(id).ownerId(), userDetails);

        tradeNoteService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
