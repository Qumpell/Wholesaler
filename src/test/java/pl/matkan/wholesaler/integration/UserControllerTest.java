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
import pl.matkan.wholesaler.contactperson.ContactPersonRepository;
import pl.matkan.wholesaler.role.Role;
import pl.matkan.wholesaler.role.RoleRepository;
import pl.matkan.wholesaler.tradenote.TradeNote;
import pl.matkan.wholesaler.tradenote.TradeNoteRepository;
import pl.matkan.wholesaler.user.User;
import pl.matkan.wholesaler.user.UserRepository;
import pl.matkan.wholesaler.user.UserRequest;
import pl.matkan.wholesaler.user.UserResponse;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.0.1");

    @LocalServerPort
    int randomServerPort;

    @Autowired
    TestRestTemplate restTemplate;

    RestClient restClient;
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    TradeNoteRepository tradeNoteRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ContactPersonRepository contactPersonRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Role role;
    private User owner;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort);
        
        role = new Role(null, "admin", new ArrayList<>());
        role = roleRepository.save(role);
        
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
                role,
                false);

        owner = userRepository.save(owner);
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }


    @Test
    void testConnection() {
        assertThat(mysql.isCreated()).isTrue();
        assertThat(mysql.isRunning()).isTrue();
    }

    @Test
    void shouldFindAllUsers() {
        
        //when
        ResponseEntity<RestPageImpl<UserResponse>> responseEntity = restClient
                .get()
                .uri("/users")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        RestPageImpl<UserResponse> body = responseEntity.getBody();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(1, body.getTotalElements())
        );
    }


    @Test
    void shouldGetOneUser_GivenValidId() {
        //given
        //when
        ResponseEntity<UserResponse> responseEntity = restClient.get()
                .uri("/users/{id}", owner.getId())
                .retrieve()
                .toEntity(UserResponse.class);

        UserResponse responseEntityBody = responseEntity.getBody();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(owner.getId(), responseEntityBody.id()),
                () -> assertEquals(owner.getUsername(), responseEntityBody.username()),
                () -> assertEquals(owner.getFirstname(), responseEntityBody.firstname()),
                () -> assertEquals(owner.getSurname(), responseEntityBody.surname()),
                () -> assertEquals(owner.getRole().getId(), responseEntityBody.roleId()),
                () -> assertEquals(owner.getRole().getName(), responseEntityBody.roleName()),
                () -> assertEquals(owner.getDateOfBirth(), responseEntityBody.dateOfBirth())
              );

    }

    @Test
    void shouldReturnNotFound_WhenGetOneUser_GivenInvalidId() {

        //when
        ResponseEntity<UserResponse> responseEntity = restTemplate.
                exchange("/users/100",
                        HttpMethod.GET,
                        null,
                        UserResponse.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnConflict_WhenCreateUser_WithAlreadyExistingUsername() {

        //given
       UserRequest userRequest = new UserRequest(
               "test",
               "test",
               "test@test.com",
               LocalDate.of(1999, Month.AUGUST, 22),
               "testLogin",
               "pass",
               role.getId()
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.
                postForEntity(
                        "/users",
                        userRequest,
                        String.class
                );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldCreateUser_GivenValidData() {

        //given
        UserRequest userRequest = new UserRequest(
                "test1",
                "test1",
                "test1@test.com",
                LocalDate.of(1999, Month.AUGUST, 22),
                "testLogin1",
                "pas23s",
                role.getId()
        );

        //when
        ResponseEntity<UserResponse> responseEntity = restClient.post()
                .uri("/users")
                .contentType(APPLICATION_JSON)
                .body(userRequest)
                .retrieve()
                .toEntity(UserResponse.class);

        UserResponse responseEntityBody = responseEntity.getBody();

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                () -> assertEquals(userRequest.firstname(), responseEntityBody.firstname()),
                () -> assertEquals(userRequest.surname(), responseEntityBody.surname()),
                () -> assertEquals(userRequest.username(), responseEntityBody.username()),
                () -> assertEquals(userRequest.email(), responseEntityBody.email()),
                () -> assertEquals(userRequest.dateOfBirth(), responseEntityBody.dateOfBirth()),
                () -> assertEquals(userRequest.roleId(), responseEntityBody.roleId())
        );
    }

    @Test
    void shouldReturnBadRequest_WhenCreateUser_GivenInvalidRoleId() {

        //given
        UserRequest userRequest = new UserRequest(
                "test1",
                "test1",
                "test1@test.com",
                LocalDate.of(1999, Month.AUGUST, 22),
                "testLogin1",
                "pas23s",
                100L
        );

        //when
        ResponseEntity<UserResponse> responseEntity = restTemplate.
                postForEntity(
                        "/users",
                        userRequest,
                        UserResponse.class
                );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldUpdateUser() {

        //given
        UserRequest userRequest = new UserRequest(
                "test1",
                "test1",
                "test1@test.com",
                LocalDate.of(1999, Month.AUGUST, 22),
                "testLogin1",
                "pas23s",
                role.getId()
        );

        //when
        ResponseEntity<UserResponse> responseEntity = restClient
                .put()
                .uri("/users/{id}", owner.getId())
                .contentType(APPLICATION_JSON)
                .body(userRequest)
                .retrieve()
                .toEntity(UserResponse.class);


        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(userRequest.firstname(), Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).firstname())),
                () -> assertNotEquals(userRequest.firstname(), owner.getFirstname())
        );
    }

    @Test
    void shouldReturnNotFound_WhenUpdateUser_GivenInvalidId() {

        //given
        UserRequest userRequest = new UserRequest(
                "test1",
                "test1",
                "test1@test.com",
                LocalDate.of(1999, Month.AUGUST, 22),
                "testLogin1",
                "pas23s",
                role.getId()
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/users/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(userRequest),
                        String.class,
                        100
                );


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnBadRequest_WhenUpdateUser_GivenNonexistentRoleId() {

        //given
        UserRequest userRequest = new UserRequest(
                "test1",
                "test1",
                "test1@test.com",
                LocalDate.of(1999, Month.AUGUST, 22),
                "testLogin1",
                "pas23s",
                100L
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/users/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(userRequest),
                        String.class,
                        owner.getId()
                );


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldDeleteRelatedTradeNotes_WhenDeleteOwner() {

        //given
        Company company = new Company(
                null,
                1234567890,
                1234567890,
                "Tech Innovations Ltd.",
                "New York",
                "123 Tech Lane",
                null,
                owner,
                new ArrayList<>(),
                new ArrayList<>(),
                false);
        company = companyRepository.save(company);

        TradeNote tradeNote = new TradeNote(null, "test content 0", company, owner, false);
        TradeNote tradeNote1 = new TradeNote(null, "test content 1",company, owner, false);
        TradeNote tradeNote2 = new TradeNote(null, "test content 2", company, owner, false);
        tradeNoteRepository.saveAll(List.of(tradeNote, tradeNote1, tradeNote2));


        //when
        ResponseEntity<UserResponse> responseEntity = restClient
                .delete()
                .uri("/users/{id}", owner.getId())
                .retrieve()
                .toEntity(UserResponse.class);

        List<TradeNote> tradeNotes = tradeNoteRepository.findAll();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode()),
                () -> assertEquals(0, tradeNotes.size())
        );
    }

    @Test
    void shouldDeleteUser() {
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
                () -> assertFalse(userRepository.existsById(id))
        );
    }

    @Test
    void shouldReturnNotFound_WhenDeleteUser_GivenInvalidID() {
        //given //when
        ResponseEntity<String> response = restTemplate
                .exchange(
                        "/users/{id}",
                        HttpMethod.DELETE,
                        null,
                        String.class,
                        100
                );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
