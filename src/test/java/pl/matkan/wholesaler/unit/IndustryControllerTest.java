package pl.matkan.wholesaler.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.industry.Industry;
import pl.matkan.wholesaler.industry.IndustryController;
import pl.matkan.wholesaler.industry.IndustryRequest;
import pl.matkan.wholesaler.industry.IndustryService;

import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IndustryController.class)
class IndustryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    IndustryService service;

    private Page<Industry> industriesPage;
    private Industry industry;
    private IndustryRequest industryRequest;

    @BeforeEach
    void setUp() {

        industry = new Industry(
                1L,
                "IT"
        );
        industryRequest = new IndustryRequest(
                "IT"
        );

        industriesPage = new PageImpl<>(List.of(industry));
    }

    @Test
    void shouldFindAllIndustries() throws Exception {
        //when //then
        when(service.findIndustriesWithPaginationAndSort(0, 10, "id", "asc"))
                .thenReturn(industriesPage);

        mockMvc.perform(get("/industries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(industriesPage.getSize())));
    }


    @Test
    void shouldGetOneIndustry() throws Exception {
        //when //then
        when(service.findById(1L)).thenReturn(industry);

        mockMvc.perform(get("/industries/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("IT")));

    }
    @Test
    void shouldReturnNotFound_WhenGetOneIndustry_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenThrow(new EntityNotFoundException("Industry not found", "with id:=" + id));

        mockMvc.perform(get("/industries/{id}", id))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldCreateIndustry() throws Exception {
        //when //then
        when(service.create(any(IndustryRequest.class))).thenReturn(industry);

        mockMvc.perform(post("/industries")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(industryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("IT")));
    }

    @Test
    void shouldReturnConflict_WhenCreateIndustry_GivenNameAlreadyExists() throws Exception {
        //when //then
        when(service.create(any(IndustryRequest.class))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/industries")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(industryRequest)))
                .andExpect(status().isConflict());

    }

    @Test
    void shouldUpdateIndustry() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        when(service.update(any(Long.class), any(IndustryRequest.class))).thenReturn(industry);

        mockMvc.perform(put("/industries/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(industryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("IT")));
    }
    @Test
    void shouldReturnNotFound_WhenUpdateIndustry_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(put("/industries/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(industryRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteIndustry() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(service).deleteById(id);

        mockMvc.perform(delete("/industries/{id}", id))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldReturnNotFound_WhenDeleteIndustry_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/industries/{id}", id))
                .andExpect(status().isNotFound());
    }


}