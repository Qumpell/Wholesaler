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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.matkan.wholesaler.role.Role;
import pl.matkan.wholesaler.user.*;
import pl.matkan.wholesaler.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@WithMockUser(username = "test", roles = {"ADMIN", "MODERATOR", "USER"})
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService service;


    private Page<UserResponse> companiesPage;
    private UserResponse userResponse;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        Role role = new Role(1L, "ADMIN", new HashSet<>());

        userResponse = new UserResponse(
                1L,
                "Test",
                "Test",
                "test@test.com",
                LocalDate.of(2000, Month.AUGUST, 22),
                "test1",
                Set.of(role));

        userRequest = new UserRequest(
                "Test",
                "Test",
                "test@test.com",
                LocalDate.of(2000, Month.AUGUST, 22),
                "test1",
                "test1234",
                Set.of(role.getId())
        );

        companiesPage = new PageImpl<>(List.of(userResponse));
    }

    @Test
    void shouldFindAllUsers() throws Exception {
        //when //then
        when(service.findAll(0, 10, "id", "asc"))
                .thenReturn(companiesPage);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(companiesPage.getSize())));
    }


    @Test
    void shouldGetOneUser() throws Exception {
        //when //then
        when(service.findById(1L)).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("Test")));
    }

    @Test
    void shouldReturnNotFound_WhenGetOneUser_GivenInvalidID() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenThrow(new ResourceNotFoundException("User not found", "with id:=" + id));

        mockMvc.perform(get("/api/users/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateUser() throws Exception {
        //when //then
        when(service.create(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/users/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("Test")));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        when(service.update(any(Long.class), any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("Test")));
    }

    @Test
    void shouldReturnNotFound_WhenUpdateUser_GivenInvalidID() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(service).deleteById(id);

        mockMvc.perform(delete("/api/users/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFound_WhenDeleteUser_GivenInvalidID() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/api/users/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
