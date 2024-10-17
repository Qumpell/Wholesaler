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
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.matkan.wholesaler.RestPageImpl;
import pl.matkan.wholesaler.auth.LoginRequest;
import pl.matkan.wholesaler.auth.jwt.JwtResponse;
import pl.matkan.wholesaler.auth.refreshtoken.RefreshTokenRepository;
import pl.matkan.wholesaler.company.Company;
import pl.matkan.wholesaler.company.CompanyRepository;
import pl.matkan.wholesaler.contactperson.ContactPerson;
import pl.matkan.wholesaler.contactperson.ContactPersonRepository;
import pl.matkan.wholesaler.contactperson.ContactPersonRequest;
import pl.matkan.wholesaler.contactperson.ContactPersonResponse;
import pl.matkan.wholesaler.industry.Industry;
import pl.matkan.wholesaler.industry.IndustryRepository;
import pl.matkan.wholesaler.role.Role;
import pl.matkan.wholesaler.role.RoleRepository;
import pl.matkan.wholesaler.tradenote.TradeNoteRequest;
import pl.matkan.wholesaler.user.User;
import pl.matkan.wholesaler.user.UserRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContactPersonControllerTest {

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
    ContactPersonRepository contactPersonRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private ContactPerson contactPerson;
    private User owner;
    private Company company;
    private String accessToken;


    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort);

        Role role = new Role(null, "USER", new HashSet<>());
        role = roleRepository.save(role);

        Industry industry = new Industry(1L, "IT", new ArrayList<>());
        industry = industryRepository.save(industry);

        owner = new User(
                null,
                "test",
                "test",
                "test@test.com",
                LocalDate.of(1999, Month.AUGUST, 22),
                "testLogin",
                "$2a$10$uFUle8nQVHyBsSuwR286uubAxVZYF4qsZpvb6RAkbQvJ6AlRU2NGy", //test1234
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                Set.of(role),
                false);

        owner = userRepository.save(owner);

        company = new Company(
                null,
                "PL1234567890",
                "987654321",
                "Tech Innovations Ltd.",
                "New York",
                "123 Tech Lane",
                industry,
                owner,
                new ArrayList<>(),
                new ArrayList<>(),
                false);


        company = companyRepository.save(company);
        contactPerson = new ContactPerson(
                null,
                "test",
                "test",
                "+48 111-222-333",
                "test@test.com",
                "Project Manager",
                company,
                owner,
                false
        );
        contactPerson = contactPersonRepository.save(contactPerson);

        accessToken = authenticateAndGetToken(owner);
    }

    private String authenticateAndGetToken(User user) {
        LoginRequest loginRequest = new LoginRequest("testLogin", "test1234");
        ResponseEntity<JwtResponse> response = restTemplate.postForEntity(
                "/api/auth/signin",
                loginRequest,
                JwtResponse.class
        );
        return Objects.requireNonNull(response.getBody()).accessToken();
    }

    @AfterEach
    void cleanUp() {
        refreshTokenRepository.deleteAll();
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
    void shouldFindAllContactPersons() {

        //given
        //when
        ResponseEntity<RestPageImpl<ContactPersonResponse>> responseEntity = restClient
                .get()
                .uri("/api/contact-persons")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        RestPageImpl<ContactPersonResponse> body = responseEntity.getBody();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(1, Objects.requireNonNull(body).getTotalElements())
        );
    }
    @Test
    void shouldFindAllContactPersonsForGivenUserId() {

        //given
        //when
        ResponseEntity<RestPageImpl<ContactPersonResponse>> responseEntity = restClient
                .get()
                .uri("/api/contact-persons/{user_id}/all", owner.getId())
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        RestPageImpl<ContactPersonResponse> body = responseEntity.getBody();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(1, Objects.requireNonNull(body).getTotalElements())
        );
    }

    @Test
    void shouldGetOneContactPerson_GivenValidID() {
        //given
        //when
        ResponseEntity<ContactPersonResponse> responseEntity = restClient.get()
                .uri("/api/contact-persons/{id}", contactPerson.getId())
                .retrieve()
                .toEntity(ContactPersonResponse.class);

        ContactPersonResponse responseEntityBody = responseEntity.getBody();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(contactPerson.getId(), Objects.requireNonNull(responseEntityBody).id()),
                () -> assertEquals(contactPerson.getFirstname(), Objects.requireNonNull(responseEntityBody).firstname()),
                () -> assertEquals(contactPerson.getSurname(), Objects.requireNonNull(responseEntityBody).surname()),
                () -> assertEquals(contactPerson.getMail(), Objects.requireNonNull(responseEntityBody).mail()),
                () -> assertEquals(contactPerson.getPosition(), Objects.requireNonNull(responseEntityBody).position()),
                () -> assertEquals(contactPerson.getPhoneNumber(), Objects.requireNonNull(responseEntityBody).phoneNumber()),
                () -> assertEquals(contactPerson.getCompany().getName(), Objects.requireNonNull(responseEntityBody).companyName()),
                () -> assertEquals(contactPerson.getCompany().getId(), Objects.requireNonNull(responseEntityBody).companyId()),
                () -> assertEquals(contactPerson.getUser().getUsername(), Objects.requireNonNull(responseEntityBody).ownerUsername()),
                () -> assertEquals(contactPerson.getUser().getId(), Objects.requireNonNull(responseEntityBody).ownerId())
        );

    }

    @Test
    void shouldReturnNotFound_WhenGetOneContactPerson_GivenInvalidID() {

        //when
        ResponseEntity<ContactPersonResponse> responseEntity = restTemplate.
                exchange("/api/contact-persons/100",
                        HttpMethod.GET,
                        null,
                        ContactPersonResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldCreateContactPerson_GivenValidData() {

        //given
        ContactPersonRequest contactPersonRequest = new ContactPersonRequest(
                "test",
                "test",
                "+48 111-222-333",
                "test@test.com",
                "Project Manager",
                company.getId()
                );

        //when
        ResponseEntity<ContactPersonResponse> responseEntity = restClient.post()
                .uri("/api/contact-persons")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(APPLICATION_JSON)
                .body(contactPersonRequest)
                .retrieve()
                .toEntity(ContactPersonResponse.class);

        ContactPersonResponse responseEntityBody = responseEntity.getBody();

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                () -> assertEquals(contactPersonRequest.firstname(), Objects.requireNonNull(responseEntityBody).firstname()),
                () -> assertEquals(contactPersonRequest.surname(), Objects.requireNonNull(responseEntityBody).surname()),
                () -> assertEquals(contactPersonRequest.mail(), Objects.requireNonNull(responseEntityBody).mail()),
                () -> assertEquals(contactPersonRequest.position(), Objects.requireNonNull(responseEntityBody).position()),
                () -> assertEquals(contactPersonRequest.phoneNumber(), Objects.requireNonNull(responseEntityBody).phoneNumber()),
                () -> assertEquals(contactPersonRequest.companyId(), Objects.requireNonNull(responseEntityBody).companyId()),
                () -> assertEquals(owner.getId(), Objects.requireNonNull(responseEntityBody).companyId())

        );
    }

//    @Test
//    void shouldReturnBadRequest_WhenCreateContactPerson_GivenInvalidOwnerId() {
//
//        //given
//        ContactPersonRequest contactPersonRequest = new ContactPersonRequest(
//                "test",
//                "test",
//                "+48 111-222-333",
//                "test@test.com",
//                "Project Manager",
//                company.getId(),
//                100L
//        );
//
//
//        //when
//        ResponseEntity<ContactPersonResponse> responseEntity = restTemplate.
//                postForEntity(
//                        "/api/contact-persons",
//                        contactPersonRequest,
//                        ContactPersonResponse.class
//                );
//
//        // then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//    }

    @Test
    void shouldReturnBadRequest_WhenCreateContactPerson_GivenInvalidData() {

        //given
        ContactPersonRequest contactPersonRequest = new ContactPersonRequest(
                "test",
                "test",
                "111-222-333",
                "test@test.com",
                "Project Manager",
                company.getId()
        );


        //when
        ResponseEntity<ContactPersonResponse> responseEntity = restTemplate.
                postForEntity(
                        "/api/contact-persons",
                        contactPersonRequest,
                        ContactPersonResponse.class
                );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequest_WhenCreateContactPerson_GivenInvalidCompanyId() {

        //given
        ContactPersonRequest contactPersonRequest = new ContactPersonRequest(
                "test",
                "test",
                "+48 111-222-333",
                "test@test.com",
                "Project Manager",
                100L
        );

        //when
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ContactPersonRequest> entity = new HttpEntity<>(contactPersonRequest, headers);
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/api/contact-persons",
                        HttpMethod.POST,
                        entity,
                        String.class
                );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldUpdateContactPerson() {
        //given
        ContactPersonRequest contactPersonRequest = new ContactPersonRequest(
                "newname",
                "testName",
                "+48 111-222-333",
                "test@test.com",
                "Project Manager",
                company.getId()
        );


        //when
        ResponseEntity<ContactPersonResponse> responseEntity = restClient
                .put()
                .uri("/api/contact-persons/{id}", contactPerson.getId())
                .header("Authorization", "Bearer " + accessToken)
                .contentType(APPLICATION_JSON)
                .body(contactPersonRequest)
                .retrieve()
                .toEntity(ContactPersonResponse.class);


        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(contactPersonRequest.firstname(), Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).firstname())),
                () -> assertEquals(contactPersonRequest.surname(), Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).surname())),
                () -> assertEquals(contactPersonRequest.phoneNumber(), Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).phoneNumber()))
        );
    }

    @Test
    void shouldReturnNotFound_WhenUpdateContactPerson_GivenInvalidId() {

        //given
        ContactPersonRequest contactPersonRequest = new ContactPersonRequest(
                "test",
                "test",
                "+48 111-222-333",
                "test@test.com",
                "Project Manager",
                company.getId()
        );


        //when
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/api/contact-persons/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(contactPersonRequest),
                        String.class,
                        100
                );


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

//    @Test
//    void shouldReturnBadRequest_WhenUpdateContactPerson_GivenNonexistentOwnerID() {
//
//        //given
//        ContactPersonRequest contactPersonRequest = new ContactPersonRequest(
//                "test",
//                "test",
//                "+48 111-222-333",
//                "test@test.com",
//                "Project Manager",
//                company.getId(),
//               100L
//        );
//
//        //when
//        ResponseEntity<String> responseEntity = restTemplate
//                .exchange(
//                        "/api/contact-persons/{id}",
//                        HttpMethod.PUT,
//                        new HttpEntity<>(contactPersonRequest),
//                        String.class,
//                        contactPerson.getId()
//                );
//
//
//        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//    }

    @Test
    void shouldReturnBadRequest_WhenUpdateContactPerson_GivenNonexistentCompanyId() {

        //given
        ContactPersonRequest contactPersonRequest = new ContactPersonRequest(
                "test",
                "test",
                "+48 111-222-333",
                "test@test.com",
                "Project Manager",
                100L
        );

        //when
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ContactPersonRequest> entity = new HttpEntity<>(contactPersonRequest, headers);
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/api/contact-persons/{id}",
                        HttpMethod.PUT,
                        entity,
                        String.class,
                        contactPerson.getId()
                );


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldDeleteContactPerson() {
        //given
        //when
        ResponseEntity<String> response = restClient
                .delete()
                .uri("/api/contact-persons/{id}", contactPerson.getId())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(String.class);

        //then
        final long id = contactPerson.getId();
        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertFalse(contactPersonRepository.existsById(id))
        );
    }

    @Test
    void shouldReturnNotFound_WhenDeleteContactPerson_GivenInvalidID() {

        //given //when
        ResponseEntity<String> response = restTemplate
                .exchange(
                        "/api/contact-persons/{id}",
                        HttpMethod.DELETE,
                        null,
                        String.class,
                        20
                );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
