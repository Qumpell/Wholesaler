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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.matkan.wholesaler.RestPageImpl;
import pl.matkan.wholesaler.company.Company;
import pl.matkan.wholesaler.company.CompanyRepository;
import pl.matkan.wholesaler.industry.Industry;
import pl.matkan.wholesaler.industry.IndustryRepository;
import pl.matkan.wholesaler.industry.IndustryRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndustryControllerTest {
    
    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.0.1");

    @LocalServerPort
    int randomServerPort;

    @Autowired
    TestRestTemplate restTemplate;

    RestClient restClient;

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private CompanyRepository companyRepository;
    
    private Industry industry;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort);
        
        industry = new Industry(null, "IT", new ArrayList<>());
        industry = industryRepository.save(industry);
        
    }

    @AfterEach
    void cleanUp() {
        industryRepository.deleteAll();
    }


    @Test
    void testConnection() {
        assertThat(mysql.isCreated()).isTrue();
        assertThat(mysql.isRunning()).isTrue();
    }

    @Test
    void shouldFindAllIndustries() {

        //given
        //when
        ResponseEntity<RestPageImpl<Industry>> responseEntity = restClient
                .get()
                .uri("/industries")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        RestPageImpl<Industry> body = responseEntity.getBody();
        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(1, Objects.requireNonNull(body).getTotalElements())
        );
    }


    @Test
    void shouldGetOneIndustry_GivenValidId() {
        //given
        //when
        ResponseEntity<Industry> responseEntity = restClient.get()
                .uri("/industries/{id}", industry.getId())
                .retrieve()
                .toEntity(Industry.class);

        Industry responseEntityBody = responseEntity.getBody();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(industry.getId(), Objects.requireNonNull(responseEntityBody).getId()),
                () -> assertEquals(industry.getName(), Objects.requireNonNull(responseEntityBody).getName())

        );
    }

    @Test
    void shouldReturnNotFound_WhenGetOneIndustry_GivenInvalidID() {

        //when
        ResponseEntity<Industry> responseEntity = restTemplate.
                exchange("/industries/100",
                        HttpMethod.GET,
                        null,
                        Industry.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnConflict_WhenCreateIndustry_WithAlreadyExistingName() {

        //given
        IndustryRequest industryRequest = new IndustryRequest(
           "IT"
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.
                postForEntity(
                        "/industries",
                        industryRequest,
                        String.class
                );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldCreateIndustry_GivenValidData() {

        //given
        IndustryRequest industryRequest = new IndustryRequest(
              "Finances"
        );

        //when
        ResponseEntity<Industry> responseEntity = restClient.post()
                .uri("/industries")
                .contentType(APPLICATION_JSON)
                .body(industryRequest)
                .retrieve()
                .toEntity(Industry.class);

        Industry responseEntityBody = responseEntity.getBody();

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                () -> assertEquals(industryRequest.name(), Objects.requireNonNull(responseEntityBody).getName()),
                () -> assertTrue(industryRepository.existsById(Objects.requireNonNull(responseEntityBody).getId()))
        );
    }

    @Test
    void shouldUpdateIndustry() {

        //given
        IndustryRequest industryRequest = new IndustryRequest(
               "newName"
        );

        //when
        ResponseEntity<Industry> responseEntity = restClient
                .put()
                .uri("/industries/{id}", industry.getId())
                .contentType(APPLICATION_JSON)
                .body(industryRequest)
                .retrieve()
                .toEntity(Industry.class);


        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(industryRequest.name(), Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).getName()))
        );
    }

    @Test
    void shouldReturnNotFound_WhenUpdateIndustry_GivenInvalidId() {

        //given
        IndustryRequest industryRequest = new IndustryRequest(
           "newName"
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/industries/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(industryRequest),
                        String.class,
                        100
                );


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldDeleteIndustry() {

        //given
        //when
        ResponseEntity<String> response = restClient
                .delete()
                .uri("/industries/{id}", industry.getId())
                .retrieve()
                .toEntity(String.class);

        //then
        final long id = industry.getId();
        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertFalse(industryRepository.existsById(id))
        );
    }

    @Test
    void shouldNotDeleteCompanies_WhenDeleteIndustry() {

        //given
        Company company = new Company(null,
                "PL1234567890",
                "987654321",
                "Tech Innovations Ltd.",
                "New York",
                "123 Tech Lane",
                industry,
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                false);
       Company companySaved = companyRepository.save(company);

        //when
        ResponseEntity<String> response = restClient
                .delete()
                .uri("/industries/{id}", industry.getId())
                .retrieve()
                .toEntity(String.class);

        //then
        final long id = industry.getId();

        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertFalse(industryRepository.existsById(id)),
                () -> assertTrue(companyRepository.existsById(companySaved.getId()))
        );
    }
    @Test
    void shouldReturnNotFound_WhenDeleteIndustry_GivenInvalidID() {
        //given //when
        ResponseEntity<String> response = restTemplate
                .exchange(
                        "/industries/{id}",
                        HttpMethod.DELETE,
                        null,
                        String.class,
                        20
                );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
