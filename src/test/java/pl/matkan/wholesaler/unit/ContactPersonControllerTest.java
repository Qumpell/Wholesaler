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
import pl.matkan.wholesaler.contactperson.ContactPerson;
import pl.matkan.wholesaler.contactperson.ContactPersonController;
import pl.matkan.wholesaler.contactperson.ContactPersonDto;
import pl.matkan.wholesaler.contactperson.ContactPersonService;
import pl.matkan.wholesaler.exception.EntityNotFoundException;

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


    private Page<ContactPersonDto> contactPersonPage;
    private ContactPerson contactPerson;
    private ContactPersonDto contactPersonDto;

    @BeforeEach
    void setUp() {
        contactPerson = new ContactPerson(
                "Marek",
                "Kowalski",
                "111-222-333",
                "test@gmail.com",
                "support");

        contactPersonDto = new ContactPersonDto(1L,
                "Marek",
                "Kowalski",
                "111-222-333",
                "test@gmail.com",
                "support",
                1L,
                "Test",
                1L);
        contactPersonPage = new PageImpl<>(List.of(contactPersonDto));
    }

    @Test
    void shouldFindAllContactPeople() throws Exception {
        //when //then
        when(service.findContactPeopleWithPaginationAndSort(0, 10, "id", "asc"))
                .thenReturn(contactPersonPage);

        mockMvc.perform(get("/contact-persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(contactPersonPage.getSize())));
    }


    @Test
    void shouldGetOneContactPerson() throws Exception {
        //when //then
        when(service.findById(1L)).thenReturn(contactPersonDto);

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
        when(service.findById(id)).thenThrow(new EntityNotFoundException("Contact person not found", "with id:=" + id));

        mockMvc.perform(get("/contact-persons/{id}", id))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldCreateContactPerson() throws Exception {
        //when //then
        when(service.create(any(ContactPersonDto.class))).thenReturn(contactPerson);

        mockMvc.perform(post("/contact-persons")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactPersonDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Marek")))
                .andExpect(jsonPath("$.surname", is("Kowalski")));
    }

    @Test
    void shouldUpdateContactPerson() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        when(service.update(any(Long.class), any(ContactPersonDto.class))).thenReturn(contactPerson);

        mockMvc.perform(put("/contact-persons/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactPersonDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Marek")))
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
                        .content(objectMapper.writeValueAsString(contactPersonDto)))
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