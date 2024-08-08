package pl.matkan.wholesaler.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.matkan.wholesaler.company.Company;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.tradenote.TradeNote;
import pl.matkan.wholesaler.tradenote.TradeNoteController;
import pl.matkan.wholesaler.tradenote.TradeNoteDto;
import pl.matkan.wholesaler.tradenote.TradeNoteService;
import pl.matkan.wholesaler.user.User;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeNoteController.class)
public class TradeNoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TradeNoteService service;


    private Page<TradeNoteDto> tradeNotePage;
    private TradeNote tradeNote;
    private TradeNoteDto tradeNoteDto;

    @BeforeEach
    void setUp() {
        tradeNote= new TradeNote(
                1L,
                "TEST CONTENT",
                true,
                new Company(),
                new User());

        tradeNoteDto = new TradeNoteDto(1L,
                "TEST CONTENT",
                1L,
                "TEST",
                1L);

        tradeNotePage = new PageImpl<>(List.of(tradeNoteDto));
    }

    @Test
    void shouldFindAllTradeNotes() throws Exception {
        //when //then
        when(service.findTradeNotesWithPaginationAndSort(0, 10, "id", "asc"))
                .thenReturn(tradeNotePage);

        mockMvc.perform(get("/trade-notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(tradeNotePage.getSize())));
    }


    @Test
    void shouldGetOneTradeNote() throws Exception {
        //when //then
        when(service.findById(1L)).thenReturn(tradeNoteDto);

        mockMvc.perform(get("/trade-notes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is("TEST CONTENT")))
                .andExpect(jsonPath("$.ownerId", is(1)))
                .andExpect(jsonPath("$.companyName", is("TEST")))
                .andExpect(jsonPath("$.companyId", is(1)));
    }
    @Test
    void shouldReturnNotFound_WhenGetOneTradeNote_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenThrow(new EntityNotFoundException("TradeNote not found", "with id:=" + id));

        mockMvc.perform(get("/trade-notes/{id}", id))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldCreateTradeNote() throws Exception {
        //when //then
        when(service.create(any(TradeNoteDto.class))).thenReturn(tradeNote);

        mockMvc.perform(post("/trade-notes")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tradeNoteDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void shouldUpdateTradeNote() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        when(service.update(any(Long.class), any(TradeNoteDto.class))).thenReturn(tradeNote);

        mockMvc.perform(put("/trade-notes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tradeNoteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
    @Test
    void shouldReturnNotFound_WhenUpdateTradeNote_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(put("/trade-notes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tradeNoteDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteTradeNote() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(service).deleteById(id);

        mockMvc.perform(delete("/trade-notes/{id}", id))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldReturnNotFound_WhenDeleteTradeNote_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/trade-notes/{id}", id))
                .andExpect(status().isNotFound());
    }

}