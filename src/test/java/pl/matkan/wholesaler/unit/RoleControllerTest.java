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
import pl.matkan.wholesaler.exception.ResourceNotFoundException;
import pl.matkan.wholesaler.role.Role;
import pl.matkan.wholesaler.role.RoleController;
import pl.matkan.wholesaler.role.RoleRequest;
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
    RoleService service;

    private Page<Role> rolePage;
    private Role role;
    private RoleRequest roleRequest;

    @BeforeEach
    void setUp() {

        role = new Role(
                1L,
                "ADMIN",
                new ArrayList<>()
        );

        roleRequest = new RoleRequest(
                "ADMIN"
        );

        rolePage = new PageImpl<>(List.of(role));
    }

    @Test
    void shouldFindAllRoles() throws Exception {
        //when //then
        when(service.findAll(0, 10, "id", "asc"))
                .thenReturn(rolePage);

        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(rolePage.getSize())));
    }


    @Test
    void shouldGetOneRole() throws Exception {
        //when //then
        when(service.findById(1L)).thenReturn(role);

        mockMvc.perform(get("/roles/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("ADMIN")));

    }

    @Test
    void shouldReturnNotFound_WhenGetOneRole_GivenInvalidID() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenThrow(new ResourceNotFoundException("Role not found", "with id:=" + id));

        mockMvc.perform(get("/roles/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateRole() throws Exception {
        //when //then
        when(service.create(any(RoleRequest.class))).thenReturn(role);

        mockMvc.perform(post("/roles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isCreated());
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.name", is("ADMIN")));
    }

    @Test
    void shouldReturnConflict_WhenCreateRole_GivenNameAlreadyExists() throws Exception {
        //when //then
        when(service.create(any(RoleRequest.class))).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(post("/roles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isConflict());

    }

    @Test
    void shouldUpdateRole() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        when(service.update(any(Long.class), any(RoleRequest.class))).thenReturn(role);

        mockMvc.perform(put("/roles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
//                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("ADMIN")));
    }

    @Test
    void shouldReturnNotFound_WhenUpdateRole_GivenInvalidID() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(put("/roles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteRole() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(service).deleteById(id);

        mockMvc.perform(delete("/roles/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFound_WhenDeleteRole_GivenInvalidID() throws Exception {
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/roles/{id}", id))
                .andExpect(status().isNotFound());
    }

}