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
import pl.matkan.wholesaler.company.CompanyController;
import pl.matkan.wholesaler.company.CompanyDto;
import pl.matkan.wholesaler.exception.EntityNotFoundException;
import pl.matkan.wholesaler.industry.Industry;
import pl.matkan.wholesaler.company.CompanyService;
import pl.matkan.wholesaler.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CompanyService service;


    private Page<CompanyDto> companiesPage;
    private Company company;
    private CompanyDto companyDto;

    @BeforeEach
    void setUp() {
        company= new Company(1L,
                        "Test",
                        "123456789",
                        "14-048",
                        "Poznan",
                        false,
                        new Industry(),
                new ArrayList<>(),
                        new User(),
                new ArrayList<>());

         companyDto = new CompanyDto(1L,
                "Test",
                "123456789",
                "14-048",
                "Poznan",
                "IT",
                1L);
       companiesPage = new PageImpl<>(List.of(companyDto));
    }

    @Test
    void shouldFindAllCompanies() throws Exception {
        //when //then
        when(service.findCompaniesWithPaginationAndSort(0, 10, "id", "asc"))
                .thenReturn(companiesPage);

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(companiesPage.getSize())));
    }


    @Test
    void shouldGetOneCompany() throws Exception {
        //when //then
        when(service.findById(1L)).thenReturn(companyDto);

        mockMvc.perform(get("/companies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.nip", is("123456789")))
                .andExpect(jsonPath("$.address", is("14-048")))
                .andExpect(jsonPath("$.city", is("Poznan")))
                .andExpect(jsonPath("$.industryName", is("IT")))
                .andExpect(jsonPath("$.ownerId", is(1)));
    }
    @Test
    void shouldReturnNotFound_WhenGetOneCompany_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenThrow(new EntityNotFoundException("Company not found", "with id:=" + id));

        mockMvc.perform(get("/companies/{id}", id))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldCreateCompany() throws Exception {
        //when //then
        when(service.create(any(CompanyDto.class))).thenReturn(company);

        mockMvc.perform(post("/companies")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.nip", is("123456789")))
                .andExpect(jsonPath("$.address", is("14-048")))
                .andExpect(jsonPath("$.city", is("Poznan")));
    }

    @Test
    void shouldUpdateCompany() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        when(service.update(any(Long.class), any(CompanyDto.class))).thenReturn(company);

        mockMvc.perform(put("/companies/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.nip", is("123456789")))
                .andExpect(jsonPath("$.address", is("14-048")))
                .andExpect(jsonPath("$.city", is("Poznan")));
    }
    @Test
    void shouldReturnNotFound_WhenUpdateCompany_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(put("/companies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteCompany() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(service).deleteById(id);

        mockMvc.perform(delete("/companies/{id}", id))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldReturnNotFound_WhenDeleteCompany_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/companies/{id}", id))
                .andExpect(status().isNotFound());
    }

}