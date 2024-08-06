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
import pl.matkan.wholesaler.role.Role;
import pl.matkan.wholesaler.role.RoleController;
import pl.matkan.wholesaler.role.RoleDto;
import pl.matkan.wholesaler.role.RoleService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleService service;

    private Page<RoleDto> rolePage;
    private Role role;
    private RoleDto roleDto;

    @BeforeEach
    void setUp() {

        role = new Role(
                1L,
                "ADMIN",
                new ArrayList<>()
        );
        roleDto = new RoleDto(
                1L,
                "ADMIN"
        );

        rolePage = new PageImpl<>(List.of(roleDto));
    }

    @Test
    void shouldFindAllRoles() throws Exception {
        //when //then
        when(service.findRolesWithPaginationAndSort(0, 10, "id", "asc"))
                .thenReturn(rolePage);

        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(rolePage.getSize())));
    }


    @Test
    void shouldGetOneRole() throws Exception {
        //when //then
        when(service.findById(1L)).thenReturn(roleDto);

        mockMvc.perform(get("/roles/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("ADMIN")));

    }
    @Test
    void shouldReturnNotFound_WhenGetOneRole_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenThrow(new EntityNotFoundException("Role not found", "with id:=" + id));

        mockMvc.perform(get("/roles/{id}", id))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldCreateRole() throws Exception {
        //when //then
        when(service.create(any(Role.class))).thenReturn(role);

        mockMvc.perform(post("/roles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("ADMIN")));
    }

    @Test
    void shouldReturnNotConflict_WhenCreateRole_GivenNameAlreadyExists() throws Exception {
        //when //then
        when(service.create(any(Role.class))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/roles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isConflict());

    }

    @Test
    void shouldUpdateRole() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        when(service.update(any(Long.class), any(Role.class))).thenReturn(role);

        mockMvc.perform(put("/roles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("ADMIN")));
    }
    @Test
    void shouldReturnNotFound_WhenUpdateRole_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(put("/roles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteRole() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(service).deleteById(id);

        mockMvc.perform(delete("/roles/{id}", id))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldReturnNotFound_WhenDeleteRole_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/roles/{id}", id))
                .andExpect(status().isNotFound());
    }

}