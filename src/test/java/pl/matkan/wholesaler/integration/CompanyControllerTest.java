package pl.matkan.wholesaler.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.matkan.wholesaler.RestPageImpl;
import pl.matkan.wholesaler.company.Company;
import pl.matkan.wholesaler.company.CompanyRepository;
import pl.matkan.wholesaler.company.CompanyRequest;
import pl.matkan.wholesaler.company.CompanyResponse;
import pl.matkan.wholesaler.industry.Industry;
import pl.matkan.wholesaler.industry.IndustryRepository;
import pl.matkan.wholesaler.role.Role;
import pl.matkan.wholesaler.role.RoleRepository;
import pl.matkan.wholesaler.user.User;
import pl.matkan.wholesaler.user.UserRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompanyControllerTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.0.1")
            .withInitScript("init.sql");


    @LocalServerPort
    int randomServerPort;

    @Autowired
    TestRestTemplate restTemplate;
    RestClient restClient;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    IndustryRepository industryRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    private RoleRepository roleRepository;

    private Industry industry;
    private Role role;
    private User owner;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort);

        role = new Role("admin");
        role = roleRepository.save(role);

        industry = new Industry(1L, "IT");
        industry = industryRepository.save(industry);
        owner = new User(
                null,
                "test",
                "test",
                LocalDate.of(1999, Month.AUGUST, 22),
                "testLogin",
                "pass",
                "admin",
                false);

        owner = userRepository.save(owner);


    }

    @AfterEach
    void cleanUp() {
        companyRepository.deleteAll();
        industryRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }


    @Test
    void testConnection() {
        assertThat(mysql.isCreated()).isTrue();
        assertThat(mysql.isRunning()).isTrue();
    }

    @Test
    void shouldFindAllCompanies() {
        //given

        Company company = new Company(null,
                "Tech Innovations Ltd.",
                "1234567890",
                "123 Tech Lane",
                "New York",
                industry.getName(),
                owner.getId(),
                false);
        companyRepository.save(company);

        //when
        ResponseEntity<RestPageImpl<CompanyResponse>> responseEntity = restClient
                .get()
                .uri("/companies")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        RestPageImpl<CompanyResponse> body = responseEntity.getBody();
        System.out.println();
        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(1, body.getTotalElements())
        );
    }

    @Test
    void shouldGetOneCompany_GivenValidID() {
        //given
        Company company = new Company(null,
                "Tech Innovations Ltd.",
                "1234567890",
                "123 Tech Lane",
                "New York",
                industry.getName(),
                owner.getId(),
                false);
        companyRepository.save(company);

        //when
        ResponseEntity<CompanyResponse> responseEntity = restClient.get()
                .uri("/companies/{id}", company.getId())
                .retrieve()
                .toEntity(CompanyResponse.class);

        CompanyResponse responseEntityBody = responseEntity.getBody();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(company.getId(), responseEntityBody.getId()),
                () -> assertEquals(company.getName(), responseEntityBody.getName()),
                () -> assertEquals(company.getCity(), responseEntityBody.getCity()),
                () -> assertEquals(company.getNip(), responseEntityBody.getNip()),
                () -> assertEquals(company.getAddress(), responseEntityBody.getAddress()),
                () -> assertEquals(company.getIndustryName(), responseEntityBody.getIndustryName()),
                () -> assertEquals(company.getOwnerId(), responseEntityBody.getOwnerId()));

    }

    @Test
    void shouldReturnNotFound_WhenGetOneCompany_GivenInvalidID() {
        //given
        Long id = 100L;

        //when
        ResponseEntity<CompanyResponse> responseEntity = restTemplate.
                exchange("/companies/100",
                        HttpMethod.GET,
                        null,
                        CompanyResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnConflict_WhenCreateCompany_WithAlreadyExistingName() {

        //given
        Company company = new Company(1L,
                "Tech Innovators Ltd.",
                "1234567890",
                "123 Tech Lane",
                "New York",
                industry.getName(),
                owner.getId(),
                false);
        companyRepository.save(company);

        CompanyRequest companyRequest = new CompanyRequest(
                "Tech Innovators Ltd.",
                "1234567890",
                "123 Innovation Street",
                "Warsaw",
                industry.getName(),
                owner.getId()
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.
                postForEntity(
                        "/companies",
                        companyRequest,
                        String.class
                );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldCreateCompany_GivenValidData() {
        //given
        CompanyRequest companyRequest = new CompanyRequest(
                "UNIQUE NAME",
                "0000000000",
                "123 Innovation Street",
                "Warsaw",
                industry.getName(),
                owner.getId()
        );

        //when
        ResponseEntity<CompanyResponse> responseEntity = restClient.post()
                .uri("/companies")
                .contentType(APPLICATION_JSON)
                .body(companyRequest)
                .retrieve()
                .toEntity(CompanyResponse.class);

        CompanyResponse responseEntityBody = responseEntity.getBody();

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                () -> assertEquals(companyRequest.name(), responseEntityBody.getName()),
                () -> assertEquals(companyRequest.address(), responseEntityBody.getAddress()),
                () -> assertEquals(companyRequest.city(), responseEntityBody.getCity()),
                () -> assertEquals(companyRequest.nip(), responseEntityBody.getNip()),
                () -> assertEquals(companyRequest.ownerId(), responseEntityBody.getOwnerId()),
                () -> assertEquals(companyRequest.industryName(), responseEntityBody.getIndustryName())
        );
    }

    @Test
    void shouldReturnBadRequest_WhenCreateCompany_GivenInvalidOwnerId() {

        //given
        CompanyRequest companyRequest = new CompanyRequest(
                "Tech",
                "1234567890",
                "123 Innovation Street",
                "Warsaw",
                industry.getName(),
                100L
        );

        //when
        ResponseEntity<CompanyResponse> responseEntity = restTemplate.
                postForEntity(
                        "/companies",
                        companyRequest,
                        CompanyResponse.class
                );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequest_WhenCreateCompany_GivenInvalidIndustryName() {

        //given
        CompanyRequest companyRequest = new CompanyRequest(
                "Tech",
                "1234567890",
                "123 Innovation Street",
                "Warsaw",
                "wrongIndustryName",
                owner.getId()
        );

        //when
        ResponseEntity<CompanyResponse> responseEntity = restTemplate.
                postForEntity(
                        "/companies",
                        companyRequest,
                        CompanyResponse.class
                );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldUpdateCompany() throws Exception {
        //given
        Company company = new Company(1L,
                "Tech Innovators Ltd.",
                "1234567890",
                "123 Tech Lane",
                "New York",
                industry.getName(),
                owner.getId(),
                false);
        company = companyRepository.save(company);

        CompanyRequest companyRequest = new CompanyRequest(
                "New Name",
                "1234567890",
                "123 Innovation Street",
                "Warsaw",
                industry.getName(),
                owner.getId()
        );

        //when
        ResponseEntity<String> responseEntity = restClient
                .put()
                        .uri("/companies/{id}", company.getId())
                                .retrieve()
                                        .toEntity(String.class);


        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(companyRequest.name(), Objects.requireNonNull(responseEntity.getBody()))
        );
    }
}
//    @Test
//    void shouldReturnNotFound_WhenUpdateCompany_GivenInvalidID() throws Exception{
//        //given
//        Long id = 1L;
//
//        //when //then
//        when(service.existsById(id)).thenReturn(false);
//
//        mockMvc.perform(put("/companies/{id}", id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(companyDto)))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void shouldDeleteCompany() throws Exception{
//        //given
//        Long id = 1L;
//
//        //when //then
//        when(service.existsById(id)).thenReturn(true);
//        Mockito.doNothing().when(service).deleteById(id);
//
//        mockMvc.perform(delete("/companies/{id}", id))
//                .andExpect(status().isNoContent());
//    }
//    @Test
//    void shouldReturnNotFound_WhenDeleteCompany_GivenInvalidID() throws Exception{
//        //given
//        Long id = 1L;
//
//        //when //then
//        when(service.existsById(id)).thenReturn(false);
//
//        mockMvc.perform(delete("/companies/{id}", id))
//                .andExpect(status().isNotFound());
//    }

