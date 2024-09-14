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
import pl.matkan.wholesaler.contactperson.*;
import pl.matkan.wholesaler.exception.ResourceNotFoundException;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactPersonController.class)
class ContactPersonControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ContactPersonService service;


    private Page<ContactPersonResponse> contactPersonPage;
    private ContactPersonResponse contactPersonResponse;
    private ContactPersonRequest contactPersonRequest;

    @BeforeEach
    void setUp() {
        contactPersonResponse = new ContactPersonResponse(
                1L,
                "Marek",
                "Kowalski",
                "111-222-333",
                "test@gmail.com",
                "support",
                "TestCompany",
                1L,
                "testuser",
                1L);
        contactPersonRequest = new ContactPersonRequest(
                "Marek",
                "Kowalski",
                "111-222-333",
                "test@gmail.com",
                "support",
                1L,
                1L
        );

        contactPersonPage = new PageImpl<>(List.of(contactPersonResponse));
    }

    @Test
    void shouldFindAllContactPeople() throws Exception {
        //when //then
        when(service.findAll(0, 10, "id", "asc"))
                .thenReturn(contactPersonPage);

        mockMvc.perform(get("/contact-persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(contactPersonPage.getSize())));
    }


    @Test
    void shouldGetOneContactPerson() throws Exception {
        //when //then
        when(service.findById(1L)).thenReturn(contactPersonResponse);

        mockMvc.perform(get("/contact-persons/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("Marek")))
                .andExpect(jsonPath("$.surname", is("Kowalski")));
    }
    @Test
    void shouldReturnNotFound_WhenGetOneContactPerson_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenThrow(new ResourceNotFoundException("Contact person not found", "with id:=" + id));

        mockMvc.perform(get("/contact-persons/{id}", id))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldCreateContactPerson() throws Exception {
        //when //then
        when(service.create(any(ContactPersonRequest.class))).thenReturn(contactPersonResponse);

        mockMvc.perform(post("/contact-persons")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactPersonRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", is("Marek")))
                .andExpect(jsonPath("$.surname", is("Kowalski")));
    }

    @Test
    void shouldUpdateContactPerson() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        when(service.update(any(Long.class), any(ContactPersonRequest.class))).thenReturn(contactPersonResponse);

        mockMvc.perform(put("/contact-persons/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactPersonRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", is("Marek")))
                .andExpect(jsonPath("$.surname", is("Kowalski")));
    }
    @Test
    void shouldReturnNotFound_WhenUpdateContactPerson_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(put("/contact-persons/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactPersonRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteContactPerson() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(service).deleteById(id);

        mockMvc.perform(delete("/contact-persons/{id}", id))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldReturnNotFound_WhenDeleteContactPerson_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/contact-persons/{id}", id))
                .andExpect(status().isNotFound());
    }

}