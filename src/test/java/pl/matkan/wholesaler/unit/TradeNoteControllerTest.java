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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.matkan.wholesaler.auth.AccessControlService;
import pl.matkan.wholesaler.auth.UserDetailsImpl;
import pl.matkan.wholesaler.exception.ResourceNotFoundException;
import pl.matkan.wholesaler.tradenote.*;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeNoteController.class)
@WithMockUser(username = "test", roles = {"ADMIN", "MODERATOR", "USER"})
public class TradeNoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TradeNoteService service;

    @MockBean
    AccessControlService accessControlService;

    private Page<TradeNoteResponse> tradeNotePage;
    private TradeNoteResponse tradeNoteResponse;
    private TradeNoteDetailedRequest tradeNoteDetailedRequest;

    @BeforeEach
    void setUp() {

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testUser", "test@test.com", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );
        tradeNoteResponse = new TradeNoteResponse(
                1L,
                "TEST CONTENT",
                "TEST",
                1L,
                "testUsername",
                1L
        );

        tradeNoteDetailedRequest = new TradeNoteDetailedRequest(
                "TEST CONTENT",
                1L,
                1L
        );

        tradeNotePage = new PageImpl<>(List.of(tradeNoteResponse));
    }

    @Test
    void shouldFindAllTradeNotes() throws Exception {
        //when //then
        when(service.findAll(0, 10, "id", "asc"))
                .thenReturn(tradeNotePage);

        mockMvc.perform(get("/api/trade-notes")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(tradeNotePage.getSize())));
    }

    @Test
    void shouldFindAllUserTradeNotes() throws Exception {
        //when //then
        when(service.findAllByUser(0, 10, "id", "asc", 1L))
                .thenReturn(tradeNotePage);

        mockMvc.perform(get("/api/trade-notes/{userId}/all", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(tradeNotePage.getSize())));
    }

    @Test
    void shouldGetOneTradeNote() throws Exception {
        //when //then
        when(service.findById(1L)).thenReturn(tradeNoteResponse);

        mockMvc.perform(get("/api/trade-notes/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is("TEST CONTENT")))
                .andExpect(jsonPath("$.ownerId", is(1)))
                .andExpect(jsonPath("$.companyName", is("TEST")));
    }

    @Test
    void shouldReturnNotFound_WhenGetOneTradeNote_GivenInvalidID() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenThrow(new ResourceNotFoundException("TradeNote was not found", "with id:=" + id));

        mockMvc.perform(get("/api/trade-notes/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateTradeNote() throws Exception {
        //when //then
        when(service.create(any(TradeNoteDetailedRequest.class))).thenReturn(tradeNoteResponse);

        mockMvc.perform(post("/api/trade-notes")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(tradeNoteDetailedRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void shouldUpdateTradeNote() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenReturn(tradeNoteResponse);
        when(service.update(any(Long.class), any(TradeNoteDetailedRequest.class))).thenReturn(tradeNoteResponse);

        mockMvc.perform(put("/api/trade-notes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(tradeNoteDetailedRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void shouldReturnNotFound_WhenUpdateTradeNote_GivenInvalidID() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenThrow(new ResourceNotFoundException("TradeNote was not found", "with id:=" + id));

        mockMvc.perform(put("/api/trade-notes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(tradeNoteDetailedRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteTradeNote() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenReturn(tradeNoteResponse);
        Mockito.doNothing().when(service).deleteById(id);

        mockMvc.perform(delete("/api/trade-notes/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFound_WhenDeleteTradeNote_GivenInvalidID() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenThrow(new ResourceNotFoundException("TradeNote was not found", "with id:=" + id));


        mockMvc.perform(delete("/api/trade-notes/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

}
