package pl.matkan.wholesaler.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;
import pl.matkan.wholesaler.RestPageImpl;
import pl.matkan.wholesaler.company.*;
import pl.matkan.wholesaler.industry.Industry;
import pl.matkan.wholesaler.industry.IndustryRepository;
import pl.matkan.wholesaler.role.Role;
import pl.matkan.wholesaler.role.RoleRepository;
import pl.matkan.wholesaler.user.User;
import pl.matkan.wholesaler.user.UserRepository;
import pl.matkan.wholesaler.user.UserService;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    @Autowired
    CompanyService companyService;



    RestClient restClient;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    IndustryRepository industryRepository;

    @Autowired
    UserRepository userRepository;

//    @BeforeAll
//    void init(){
//
//    }


    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort);

    }
    @AfterEach
    void cleanUp(){
        companyRepository.deleteAll();
        industryRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void testConnection(){
      assertThat(mysql.isCreated()).isTrue();
      assertThat(mysql.isRunning()).isTrue();
    }

    @Test
    void shouldFindAllCompanies(){
        //given
        Industry industry = new Industry("IT");
        industryRepository.save(industry);
        User user = new User(
                "test",
                "test",
                LocalDate.of(1999, Month.AUGUST,22),
                "testLogin",
                "pass",
                "admin");

        userRepository.save(user);

        Company company = new Company(1L,
                "Tech Innovations Ltd.",
                "1234567890",
                "123 Tech Lane",
                "New York",
                "IT",
                1L,
                false);
        companyRepository.save(company);

        //when
        ResponseEntity<RestPageImpl<CompanyResponse>> responseEntity = restClient
                .get()
                .uri("/companies")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {});

        RestPageImpl<CompanyResponse> body = responseEntity.getBody();
        System.out.println();
        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(1, body.getTotalElements())
        );
    }

    @Test
    void shouldGetOneCompany_GivenValidID(){
        //given
        Industry industry = new Industry("IT");
        industryRepository.save(industry);
        User user = new User(
                "test",
                "test",
                LocalDate.of(1999, Month.AUGUST,22),
                "testLogin",
                "pass",
                "admin");

        userRepository.save(user);

        Company company = new Company(2L,
                "Tech Innovations Ltd.",
                "1234567890",
                "123 Tech Lane",
                "New York",
                "IT",
                1L,
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
               ()->assertEquals(company.getId(), responseEntityBody.getId()),
               ()->assertEquals(company.getName(), responseEntityBody.getName()),
               ()->assertEquals(company.getCity(), responseEntityBody.getCity()),
               ()->assertEquals(company.getNip(), responseEntityBody.getNip()),
               ()->assertEquals(company.getAddress(), responseEntityBody.getAddress()),
               ()->assertEquals(company.getIndustryName(), responseEntityBody.getIndustryName()),
               ()->assertEquals(company.getOwnerId(), responseEntityBody.getOwnerId()));

    }
    @Test
    void shouldReturnNotFound_WhenGetOneCompany_GivenInvalidID(){
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
    void shouldReturnConflict_WhenCreateCompany_WithAlreadyExistName(){

       //given
        CompanyRequest companyRequest = new CompanyRequest(
                "Tech Innovators Ltd.",
                "1234567890",
                "123 Innovation Street",
                "Warsaw",
                "IT",
                1L
        );

        //when
        ResponseEntity<CompanyResponse> responseEntity = restTemplate.
                postForEntity(
                        "/companies",
                        companyRequest,
                        CompanyResponse.class
                );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @Transactional
    void shouldCreateCompany_GivenValidData(){
       //given
        CompanyRequest companyRequest = new CompanyRequest(
                "UNIQUE NAME",
                "0000000000",
                "123 Innovation Street",
                "Warsaw",
                "IT",
                1L
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
//                () -> assertEquals(11L, responseEntityBody.getId()),
                () -> assertEquals(companyRequest.name(), responseEntityBody.getName()),
                () -> assertEquals(companyRequest.address(), responseEntityBody.getAddress()),
                () -> assertEquals(companyRequest.city(), responseEntityBody.getCity()),
                () -> assertEquals(companyRequest.nip(), responseEntityBody.getNip()),
                () -> assertEquals(companyRequest.ownerId(), responseEntityBody.getOwnerId()),
                () -> assertEquals(companyRequest.industryName(), responseEntityBody.getIndustryName()),
//                () -> assertEquals(companyRequest.ownerId(), responseEntityBody.getUser().getId()),
//                () -> assertEquals(companyRequest.industryName(), responseEntityBody.getIndustry().getName()),
                () -> assertTrue(companyService.existsById(responseEntityBody.getId()))

        );
    }

    @Test
    void shouldReturnBadRequest_WhenCreateCompany_GivenInvalidOwnerId(){

        //given
        CompanyRequest companyRequest = new CompanyRequest(
                "Tech",
                "1234567890",
                "123 Innovation Street",
                "Warsaw",
                "it",
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
    void shouldReturnBadRequest_WhenCreateCompany_GivenInvalidIndustryName(){

        //given
        CompanyRequest companyRequest = new CompanyRequest(
                "Tech",
                "1234567890",
                "123 Innovation Street",
                "Warsaw",
                "wrong",
                1L
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

//    @Test
//    void shouldUpdateCompany() throws Exception{
//        //given
//        Long id = 1L;
//
//        //when //then
//        when(service.existsById(id)).thenReturn(true);
//        when(service.update(any(Long.class), any(CompanyDto.class))).thenReturn(company);
//
//        mockMvc.perform(put("/companies/{id}", id)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(companyDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.name", is("Test")))
//                .andExpect(jsonPath("$.nip", is("123456789")))
//                .andExpect(jsonPath("$.address", is("14-048")))
//                .andExpect(jsonPath("$.city", is("Poznan")));
//    }
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

}