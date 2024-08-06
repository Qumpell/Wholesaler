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
import pl.matkan.wholesaler.role.Role;
import pl.matkan.wholesaler.user.User;
import pl.matkan.wholesaler.user.UserDto;
import pl.matkan.wholesaler.user.UserService;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.user.UserController;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService service;


    private Page<UserDto> companiesPage;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
      user = new User(1L,
              "Test",
              "Test",
              LocalDate.of(2000, Month.AUGUST,22),
              "test",
              "123" ,
              false,
              new ArrayList<>(),
              new ArrayList<>(),
              new ArrayList<>(),
              new Role("ADMIN")
      );

        userDto = new UserDto(
                1L,
                "Test",
                "Test",
                LocalDate.of(2000, Month.AUGUST,22),
                "test",
                "ADMIN");
        companiesPage = new PageImpl<>(List.of(userDto));
    }

    @Test
    void shouldFindAllUsers() throws Exception {
        //when //then
        when(service.findUsersWithPaginationAndSort(0, 10, "id", "asc"))
                .thenReturn(companiesPage);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(companiesPage.getSize())));
    }


    @Test
    void shouldGetOneUser() throws Exception {
        //when //then
        when(service.findById(1L)).thenReturn(userDto);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("Test")));
    }
    @Test
    void shouldReturnNotFound_WhenGetOneUser_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenThrow(new EntityNotFoundException("User not found", "with id:=" + id));

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldCreateUser() throws Exception {
        //when //then
        when(service.create(any(UserDto.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")));
    }

    @Test
    void shouldUpdateUser() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        when(service.update(any(Long.class), any(UserDto.class))).thenReturn(user);

        mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")));
    }
    @Test
    void shouldReturnNotFound_WhenUpdateUser_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(put("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteUser() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(service).deleteById(id);

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldReturnNotFound_WhenDeleteUser_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isNotFound());
    }
}
