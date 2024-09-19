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
import pl.matkan.wholesaler.company.CompanyRequest;
import pl.matkan.wholesaler.company.CompanyResponse;
import pl.matkan.wholesaler.contactperson.ContactPersonRepository;
import pl.matkan.wholesaler.industry.Industry;
import pl.matkan.wholesaler.industry.IndustryRepository;
import pl.matkan.wholesaler.role.Role;
import pl.matkan.wholesaler.role.RoleRepository;
import pl.matkan.wholesaler.tradenote.TradeNote;
import pl.matkan.wholesaler.tradenote.TradeNoteRepository;
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
class CompanyControllerTest {

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

    @Autowired
    ContactPersonRepository contactPersonRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Industry industry;
    private Role role;
    private User owner;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort);

        role = new Role(null, "admin", new HashSet<>());
        role = roleRepository.save(role);

        industry = new Industry(1L, "IT", new ArrayList<>());
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
                Set.of(role),
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
        companyRepository.save(company);

        //when
        ResponseEntity<RestPageImpl<CompanyResponse>> responseEntity = restClient
                .get()
                .uri("/companies")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        RestPageImpl<CompanyResponse> body = responseEntity.getBody();

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
                () -> assertEquals(company.getId(), responseEntityBody.id()),
                () -> assertEquals(company.getName(), responseEntityBody.name()),
                () -> assertEquals(company.getCity(), responseEntityBody.city()),
                () -> assertEquals(company.getNip(), responseEntityBody.nip()),
                () -> assertEquals(company.getAddress(), responseEntityBody.address()),
                () -> assertEquals(company.getIndustry().getName(), responseEntityBody.industryName()),
                () -> assertEquals(company.getIndustry().getId(), responseEntityBody.industryId()),
                () -> assertEquals(company.getUser().getId(), responseEntityBody.ownerId()),
                () -> assertEquals(company.getUser().getUsername(), responseEntityBody.ownerUsername()));

    }

    @Test
    void shouldReturnNotFound_WhenGetOneCompany_GivenInvalidID() {

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
    void shouldReturnConflict_WhenCreateCompany_WithAlreadyExistingNip() {

        //given
        Company company = new Company(null,
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
        companyRepository.save(company);

        CompanyRequest companyRequest = new CompanyRequest(
                "1234567890",
                "1234567890",
                "Tech Innovators Ltd.",
                "1234567890",
                "123 Innovation Street",
                industry.getId(),
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
                "1234567890",
                "1234567890",
                "UNIQUE NAME",
                "123 Innovation Street",
                "Warsaw",
                industry.getId(),
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
                () -> assertEquals(companyRequest.name(), responseEntityBody.name()),
                () -> assertEquals(companyRequest.address(), responseEntityBody.address()),
                () -> assertEquals(companyRequest.city(), responseEntityBody.city()),
                () -> assertEquals(companyRequest.nip(), responseEntityBody.nip()),
                () -> assertEquals(companyRequest.regon(), responseEntityBody.regon()),
                () -> assertEquals(companyRequest.ownerId(), responseEntityBody.ownerId()),
                () -> assertEquals(companyRequest.industryId(), responseEntityBody.industryId())
        );
    }

    @Test
    void shouldReturnBadRequest_WhenCreateCompany_GivenInvalidOwnerId() {

        //given
        CompanyRequest companyRequest = new CompanyRequest(
                "1234567890",
                "1234567890",
                "Tech",
                "123 Innovation Street",
                "Warsaw",
                industry.getId(),
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
    void shouldReturnBadRequest_WhenCreateCompany_GivenInvalidIndustryId() {

        //given
        CompanyRequest companyRequest = new CompanyRequest(
                "1234567890",
                "1234567890",
                "Tech",
                "123 Innovation Street",
                "Warsaw",
                100L,
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
    void shouldUpdateCompany() {


        //given
        Company company = new Company(null,
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

        CompanyRequest companyRequest = new CompanyRequest(
                "1234567890",
                "1234567890",
                "New Name",
                "123 Innovation Street",
                "Warsaw",
                industry.getId(),
                owner.getId()
        );

        //when
        ResponseEntity<CompanyResponse> responseEntity = restClient
                .put()
                .uri("/companies/{id}", company.getId())
                .contentType(APPLICATION_JSON)
                .body(companyRequest)
                .retrieve()
                .toEntity(CompanyResponse.class);


        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(companyRequest.name(), Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).name()))
        );
    }

    @Test
    void shouldReturnNotFound_WhenUpdateCompany_GivenInvalidId() {

        //given
        Company company = new Company(null,
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

        CompanyRequest companyRequest = new CompanyRequest(
                "1234567890",
                "1234567890",
                "New Name",
                "123 Innovation Street",
                "Warsaw",
                industry.getId(),
                owner.getId()
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/companies/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(companyRequest),
                        String.class,
                        100
                );


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnBadRequest_WhenUpdateCompany_GivenNonexistentOwnerID() {

        //given
        Company company = new Company(null,
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

        CompanyRequest companyRequest = new CompanyRequest(
                "1234567890",
                "1234567890",
                "New Name",
                "123 Innovation Street",
                "Warsaw",
                industry.getId(),
                100L
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/companies/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(companyRequest),
                        String.class,
                        company.getId()
                );


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequest_WhenUpdateCompany_GivenNonexistentIndustryId() {

        //given
        Company company = new Company(
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

        CompanyRequest companyRequest = new CompanyRequest(
                "1234567890",
                "1234567890",
                "New Name",
                "123 Innovation Street",
                "Warsaw",
                100L,
                owner.getId()
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/companies/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(companyRequest),
                        String.class,
                        company.getId()
                );


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldDeleteRelatedTradeNotes_WhenDeleteCompany() {

        //given
        Company company = new Company(
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

        TradeNote tradeNote = new TradeNote(null, "test content 0", company, owner, false);
        TradeNote tradeNote1 = new TradeNote(null, "test content 1", company, owner, false);
        TradeNote tradeNote2 = new TradeNote(null, "test content 2", company, owner, false);
        tradeNoteRepository.saveAll(List.of(tradeNote, tradeNote1, tradeNote2));


        //when
        ResponseEntity<CompanyResponse> responseEntity = restClient
                .delete()
                .uri("/companies/{id}", company.getId())
                .retrieve()
                .toEntity(CompanyResponse.class);

        List<TradeNote> tradeNotes = tradeNoteRepository.findAll();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode()),
                () -> assertEquals(0, tradeNotes.size())
        );
    }

    @Test
    void shouldDeleteCompany() {
        //given
        Company company = new Company(
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

        //when
        ResponseEntity<String> response = restClient
                .delete()
                .uri("/companies/{id}", company.getId())
                .retrieve()
                .toEntity(String.class);

        //then
        final long id = company.getId();
        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertFalse(companyRepository.existsById(id))
        );
    }
    @Test
    void shouldNotDeleteRelatedIndustry_WhenDeleteCompany() {
        //given
        //when
        ResponseEntity<String> response = restClient
                .delete()
                .uri("/users/{id}", owner.getId())
                .retrieve()
                .toEntity(String.class);

        //then
        final long id = owner.getId();
        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertFalse(userRepository.existsById(id)),
                () -> assertTrue(industryRepository.existsById((industry.getId())))
        );
    }
    @Test
    void shouldReturnNotFound_WhenDeleteCompany_GivenInvalidID() {
        //given //when
        ResponseEntity<String> response = restTemplate
                .exchange(
                        "/companies/{id}",
                        HttpMethod.DELETE,
                        null,
                        String.class,
                        20
                );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

