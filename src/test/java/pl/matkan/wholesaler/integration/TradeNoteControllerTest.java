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
import pl.matkan.wholesaler.tradenote.TradeNote;
import pl.matkan.wholesaler.tradenote.TradeNoteRepository;
import pl.matkan.wholesaler.tradenote.TradeNoteRequest;
import pl.matkan.wholesaler.tradenote.TradeNoteResponse;
import pl.matkan.wholesaler.user.User;
import pl.matkan.wholesaler.user.UserRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TradeNoteControllerTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.0.1");

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
    TradeNoteRepository tradeNoteRepository;

    private TradeNote tradeNote;
    private User owner;
    private Company company;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort);


        Industry industry = new Industry(1L, "IT", new ArrayList<>());
        industry = industryRepository.save(industry);
        owner = new User(
                null,
                "test",
                "test",
                "test@test.com",
                LocalDate.of(1999, Month.AUGUST, 22),
                "testLogin",
                "pass",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new HashSet<>(),
                false);

        owner = userRepository.save(owner);

        company = new Company(
                null,
                "1234567890",
                "1234567890",
                "Tech Innovations Ltd.",
                "New York",
                "123 Tech Lane",
                industry,
                owner,
                new ArrayList<>(),
                new ArrayList<>(),
                false);


        company = companyRepository.save(company);
        tradeNote = new TradeNote(
                null,
                "content, consata fsaaffa",
                company,
                owner,
                false
        );
        tradeNote = tradeNoteRepository.save(tradeNote);

    }

    @AfterEach
    void cleanUp() {
        companyRepository.deleteAll();
        industryRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void testConnection() {
        assertThat(mysql.isCreated()).isTrue();
        assertThat(mysql.isRunning()).isTrue();
    }

    @Test
    void shouldFindAllTradeNotes() {

        //given
        //when
        ResponseEntity<RestPageImpl<TradeNoteResponse>> responseEntity = restClient
                .get()
                .uri("/trade-notes")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        RestPageImpl<TradeNoteResponse> body = responseEntity.getBody();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(1, Objects.requireNonNull(body).getTotalElements())
        );
    }


    @Test
    void shouldGetOneTradeNote_GivenValidID() {
        //given
        //when
        ResponseEntity<TradeNoteResponse> responseEntity = restClient.get()
                .uri("/trade-notes/{id}", tradeNote.getId())
                .retrieve()
                .toEntity(TradeNoteResponse.class);

        TradeNoteResponse responseEntityBody = responseEntity.getBody();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(tradeNote.getId(), Objects.requireNonNull(responseEntityBody).id()),
                () -> assertEquals(tradeNote.getContent(), Objects.requireNonNull(responseEntityBody).content()),
                () -> assertEquals(tradeNote.getCompany().getName(), Objects.requireNonNull(responseEntityBody).companyName()),
                () -> assertEquals(tradeNote.getCompany().getId(), Objects.requireNonNull(responseEntityBody).companyId()),
                () -> assertEquals(tradeNote.getUser().getUsername(), Objects.requireNonNull(responseEntityBody).ownerUsername()),
                () -> assertEquals(tradeNote.getUser().getId(), Objects.requireNonNull(responseEntityBody).ownerId())
        );

    }

    @Test
    void shouldReturnNotFound_WhenGetOneTradeNote_GivenInvalidID() {

        //when
        ResponseEntity<TradeNoteResponse> responseEntity = restTemplate.
                exchange("/trade-notes/100",
                        HttpMethod.GET,
                        null,
                        TradeNoteResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldCreateTradeNote_GivenValidData() {

        //given
        TradeNoteRequest tradeNoteRequest = new TradeNoteRequest(
                "test content",
                company.getId(),
                owner.getId()
        );

        //when
        ResponseEntity<TradeNoteResponse> responseEntity = restClient.post()
                .uri("/trade-notes")
                .contentType(APPLICATION_JSON)
                .body(tradeNoteRequest)
                .retrieve()
                .toEntity(TradeNoteResponse.class);

        TradeNoteResponse responseEntityBody = responseEntity.getBody();

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                () -> assertEquals(tradeNoteRequest.content(), Objects.requireNonNull(responseEntityBody).content()),
                () -> assertEquals(tradeNoteRequest.companyId(), Objects.requireNonNull(responseEntityBody).companyId()),
                () -> assertEquals(tradeNoteRequest.ownerId(), Objects.requireNonNull(responseEntityBody).ownerId())
        );
    }

    @Test
    void shouldReturnBadRequest_WhenCreateTradeNote_GivenInvalidOwnerId() {

        //given
        TradeNoteRequest tradeNoteRequest = new TradeNoteRequest(
                "1234567890",
                company.getId(),
                100L
        );

        //when
        ResponseEntity<TradeNoteResponse> responseEntity = restTemplate.
                postForEntity(
                        "/trade-notes",
                        tradeNoteRequest,
                        TradeNoteResponse.class
                );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequest_WhenCreateTradeNote_GivenInvalidCompanyId() {

        //given
        TradeNoteRequest tradeNoteRequest = new TradeNoteRequest(
                "content",
                100L,
                owner.getId()
        );

        //when
        ResponseEntity<TradeNoteResponse> responseEntity = restTemplate.
                postForEntity(
                        "/trade-notes",
                        tradeNoteRequest,
                        TradeNoteResponse.class
                );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldUpdateTradeNote() {
        //given
        TradeNoteRequest tradeNoteRequest = new TradeNoteRequest(
                "new content new",
                company.getId(),
                owner.getId()
        );

        //when
        ResponseEntity<TradeNoteResponse> responseEntity = restClient
                .put()
                .uri("/trade-notes/{id}", tradeNote.getId())
                .contentType(APPLICATION_JSON)
                .body(tradeNoteRequest)
                .retrieve()
                .toEntity(TradeNoteResponse.class);


        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(tradeNoteRequest.content(), Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).content()))
        );
    }

    @Test
    void shouldReturnNotFound_WhenUpdateTradeNote_GivenInvalidId() {

        //given
        TradeNoteRequest tradeNoteRequest = new TradeNoteRequest(
                "new content",
                company.getId(),
                owner.getId()
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/trade-notes/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(tradeNoteRequest),
                        String.class,
                        100
                );


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnBadRequest_WhenUpdateTradeNote_GivenNonexistentOwnerID() {

        //given
        TradeNoteRequest tradeNoteRequest = new TradeNoteRequest(
                "new content",
                company.getId(),
                100L
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/trade-notes/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(tradeNoteRequest),
                        String.class,
                        tradeNote.getId()
                );


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequest_WhenUpdateTradeNote_GivenNonexistentCompanyId() {

        //given
        TradeNoteRequest tradeNoteRequest = new TradeNoteRequest(
                "content test",
                100L,
                owner.getId()
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/trade-notes/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(tradeNoteRequest),
                        String.class,
                        tradeNote.getId()
                );


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldDeleteTradeNote() {
        //given
        //when
        ResponseEntity<String> response = restClient
                .delete()
                .uri("/trade-notes/{id}", tradeNote.getId())
                .retrieve()
                .toEntity(String.class);

        //then
        final long id = tradeNote.getId();
        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertFalse(tradeNoteRepository.existsById(id))
        );
    }

    @Test
    void shouldReturnNotFound_WhenDeleteTradeNote_GivenInvalidID() {

        //given //when
        ResponseEntity<String> response = restTemplate
                .exchange(
                        "/trade-notes/{id}",
                        HttpMethod.DELETE,
                        null,
                        String.class,
                        20
                );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
