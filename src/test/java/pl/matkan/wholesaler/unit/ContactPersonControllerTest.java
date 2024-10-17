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
import pl.matkan.wholesaler.contactperson.*;
import pl.matkan.wholesaler.exception.ResourceNotFoundException;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactPersonController.class)
@WithMockUser(username = "test", roles = {"ADMIN", "MODERATOR", "USER"})
class ContactPersonControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ContactPersonService service;

    @MockBean
    AccessControlService accessControlService;

    private Page<ContactPersonResponse> contactPersonPage;
    private ContactPersonResponse contactPersonResponse;
    private ContactPersonRequest contactPersonRequest;

    @BeforeEach
    void setUp() {

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testUser", "test@test.com", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        contactPersonResponse = new ContactPersonResponse(
                1L,
                "Marek",
                "Kowalski",
                "+48 111-222-333",
                "test@gmail.com",
                "support",
                "TestCompany",
                1L,
                "testuser",
                1L);
        contactPersonRequest = new ContactPersonRequest(
                "Marek",
                "Kowalski",
                "+48 111-222-333",
                "test@gmail.com",
                "support",
                1L
        );

        contactPersonPage = new PageImpl<>(List.of(contactPersonResponse));
    }

    @Test
    void shouldFindAllContactPeople() throws Exception {
        //when //then
        when(service.findAll(0, 10, "id", "asc"))
                .thenReturn(contactPersonPage);

        mockMvc.perform(get("/api/contact-persons")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(contactPersonPage.getSize())));
    }

    @Test
    void shouldFindAllUserTradeNotes() throws Exception {
        //when //then
        when(service.findAllByUser(0, 10, "id", "asc", 1L))
                .thenReturn(contactPersonPage);

        mockMvc.perform(get("/api/contact-persons/{userId}/all", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(contactPersonPage.getSize())));
    }

    @Test
    void shouldGetOneContactPerson() throws Exception {
        //when //then
        when(service.findById(1L)).thenReturn(contactPersonResponse);

        mockMvc.perform(get("/api/contact-persons/{id}", 1L)
                        .with(csrf()))
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

        mockMvc.perform(get("/api/contact-persons/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldCreateContactPerson() throws Exception {
        //when //then
        when(service.create(any(ContactPersonDetailedRequest.class))).thenReturn(contactPersonResponse);

        mockMvc.perform(post("/api/contact-persons")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
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
        when(service.findById(id)).thenReturn(contactPersonResponse);
        when(service.update(any(Long.class), any(ContactPersonDetailedRequest.class))).thenReturn(contactPersonResponse);

        mockMvc.perform(put("/api/contact-persons/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
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
        when(service.findById(id)).thenThrow(new ResourceNotFoundException("Contact person not found", "with id:=" + id));

        mockMvc.perform(put("/api/contact-persons/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(contactPersonRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteContactPerson() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenReturn(contactPersonResponse);
        Mockito.doNothing().when(service).deleteById(id);

        mockMvc.perform(delete("/api/contact-persons/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldReturnNotFound_WhenDeleteContactPerson_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenThrow(new ResourceNotFoundException("Contact person was not found", "with id:=" + id));

        mockMvc.perform(delete("/api/contact-persons/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

}