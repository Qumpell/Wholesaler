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
import pl.matkan.wholesaler.company.*;
import pl.matkan.wholesaler.exception.ResourceNotFoundException;

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


    private Page<CompanyResponse> companiesPage;
    private CompanyResponse companyResponse;
    private CompanyRequest companyRequest;

    @BeforeEach
    void setUp() {
         companyResponse = new CompanyResponse(
                 1L,
                 "1234567890",
                 "1234567890",
                 "Test",
                "14-048",
                "Poznan",
                "IT",
                1L,
                 "johnTest",
                 1L);

         companyRequest = new CompanyRequest(
                 companyResponse.nip(),
                 companyResponse.regon(),
                 companyResponse.name(),
                 companyResponse.address(),
                 companyResponse.city(),
                 companyResponse.industryId(),
                 companyResponse.ownerId()
         );
       companiesPage = new PageImpl<>(List.of(companyResponse));
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
        when(service.findById(1L)).thenReturn(companyResponse);

        mockMvc.perform(get("/companies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(companyRequest.name())))
                .andExpect(jsonPath("$.nip", is(companyRequest.nip())))
                .andExpect(jsonPath("$.address", is(companyRequest.address())))
                .andExpect(jsonPath("$.city", is(companyRequest.city())))
                .andExpect(jsonPath("$.regon", is(companyRequest.regon())))
                .andExpect(jsonPath("$.industryName", is(companyResponse.industryName())))
                .andExpect(jsonPath("$.ownerId", is(companyResponse.ownerId().intValue())));
    }
    @Test
    void shouldReturnNotFound_WhenGetOneCompany_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.findById(id)).thenThrow(new ResourceNotFoundException("Company not found", "with id:=" + id));

        mockMvc.perform(get("/companies/{id}", id))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldCreateCompany() throws Exception {
        //when //then
        when(service.create(any(CompanyRequest.class))).thenReturn(companyResponse);

        mockMvc.perform(post("/companies")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(companyRequest.name())))
                .andExpect(jsonPath("$.nip", is(companyRequest.nip())))
                .andExpect(jsonPath("$.address", is(companyRequest.address())))
                .andExpect(jsonPath("$.city", is(companyRequest.city())))
                .andExpect(jsonPath("$.regon", is(companyRequest.regon())));
    }

    @Test
    void shouldUpdateCompany() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(true);
        when(service.update(any(Long.class), any(CompanyRequest.class))).thenReturn(companyResponse);

        mockMvc.perform(put("/companies/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(companyRequest.name())))
                .andExpect(jsonPath("$.nip", is(companyRequest.nip())))
                .andExpect(jsonPath("$.address", is(companyRequest.address())))
                .andExpect(jsonPath("$.city", is(companyRequest.city())))
                .andExpect(jsonPath("$.regon", is(companyRequest.regon())));
    }
    @Test
    void shouldReturnNotFound_WhenUpdateCompany_GivenInvalidID() throws Exception{
        //given
        Long id = 1L;

        //when //then
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(put("/companies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyRequest)))
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