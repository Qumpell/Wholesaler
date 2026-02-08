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
import org.springframework.transaction.annotation.Transactional;
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
import java.util.*;

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
    @Transactional
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort);

        role = new Role(null, "admin", new HashSet<>());
        role = roleRepository.save(role);
        
        owner = new User(
                null,
                "test",
                "test",
                "test@test.com",
                LocalDate.of(1999, Month.AUGUST, 22),
                "testLogin",
                "pass1234",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                Set.of(role),
                false);

        role.getUsers().add(owner);
        owner = userRepository.save(owner);
        roleRepository.save(role);
    }

    @AfterEach
    void cleanUp() {
       List<User> users = userRepository.findAll();
       for(User user: users){
           user.setRoles(new HashSet<>());
       }
        userRepository.saveAll(users);
       List<Role> roles = roleRepository.findAll();
       for(Role role : roles){
           role.setUsers(new HashSet<>());
       }
       roleRepository.saveAll(roles);

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
                .uri("/api/users")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        RestPageImpl<UserResponse> body = responseEntity.getBody();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(1, Objects.requireNonNull(body).getTotalElements())
        );
    }


    @Test
    void shouldGetOneUser_GivenValidId() {
        //given
        //when
        ResponseEntity<UserResponse> responseEntity = restClient.get()
                .uri("/api/users/{id}", owner.getId())
                .retrieve()
                .toEntity(UserResponse.class);

        UserResponse responseEntityBody = responseEntity.getBody();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(owner.getId(), Objects.requireNonNull(responseEntityBody).id()),
                () -> assertEquals(owner.getUsername(), Objects.requireNonNull(responseEntityBody).username()),
                () -> assertEquals(owner.getFirstname(), Objects.requireNonNull(responseEntityBody).firstname()),
                () -> assertEquals(owner.getSurname(), Objects.requireNonNull(responseEntityBody).surname()),
                () -> assertEquals(owner.getRoles().isEmpty(), Objects.requireNonNull(responseEntityBody).roles().isEmpty()),
                () -> assertEquals(owner.getDateOfBirth(), Objects.requireNonNull(responseEntityBody).dateOfBirth())
              );

    }

    @Test
    void shouldReturnNotFound_WhenGetOneUser_GivenInvalidId() {

        //when
        ResponseEntity<UserResponse> responseEntity = restTemplate.
                exchange("/api/users/100",
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
               "pass1234",
               Set.of(role.getId())
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.
                postForEntity(
                        "/api/users/signup",
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
                "pass1234",
                Set.of(role.getId())
        );

        //when
        ResponseEntity<UserResponse> responseEntity = restClient.post()
                .uri("/api/users/signup")
                .contentType(APPLICATION_JSON)
                .body(userRequest)
                .retrieve()
                .toEntity(UserResponse.class);

        UserResponse responseEntityBody = responseEntity.getBody();

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                () -> assertEquals(userRequest.firstname(), Objects.requireNonNull(responseEntityBody).firstname()),
                () -> assertEquals(userRequest.surname(), Objects.requireNonNull(responseEntityBody).surname()),
                () -> assertEquals(userRequest.username(), Objects.requireNonNull(responseEntityBody).username()),
                () -> assertEquals(userRequest.email(), Objects.requireNonNull(responseEntityBody).email()),
                () -> assertEquals(userRequest.dateOfBirth(), Objects.requireNonNull(responseEntityBody).dateOfBirth()),
                () -> assertEquals(userRequest.roleIds().size(), Objects.requireNonNull(responseEntityBody).roles().size())
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
                "pass1234",
                Set.of(100L)
        );

        //when
        ResponseEntity<UserResponse> responseEntity = restTemplate.
                postForEntity(
                        "/api/users/signup",
                        userRequest,
                        UserResponse.class
                );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldUpdateUser() {
        Role role1 = new Role(null, "tester", new HashSet<>());
        roleRepository.save(role1);
        //given
        UserRequest userRequest = new UserRequest(
                "test1",
                "test1",
                "test1@test.com",
                LocalDate.of(1999, Month.AUGUST, 22),
                "testLogin1",
                "pass1234",
                Set.of(role1.getId())
        );

        //when
        ResponseEntity<UserResponse> responseEntity = restClient
                .put()
                .uri("/api/users/{id}", owner.getId())
                .contentType(APPLICATION_JSON)
                .body(userRequest)
                .retrieve()
                .toEntity(UserResponse.class);

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(userRequest.firstname(), Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).firstname())),
                () -> assertNotEquals(userRequest.firstname(), owner.getFirstname()),
                () -> assertTrue(Objects.requireNonNull(responseEntity.getBody()).roles().stream().anyMatch(r -> r.getId().equals(role1.getId()))),
                () -> assertFalse(Objects.requireNonNull(responseEntity.getBody()).roles().stream().anyMatch(r -> r.getId().equals(role.getId())))

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
                "pass1234",
                Set.of(role.getId())
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/api/users/{id}",
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
                "pass1234",
                Set.of(100L)
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/api/users/{id}",
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
                "PL1234567890",
                "987654321",
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
                .uri("/api/users/{id}", owner.getId())
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
                .uri("/api/users/{id}", owner.getId())
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
    void shouldNotDeleteRelatedRoles_WhenDeleteUser() {
        //given
        //when
        ResponseEntity<String> response = restClient
                .delete()
                .uri("/api/users/{id}", owner.getId())
                .retrieve()
                .toEntity(String.class);

        //then
        final long id = owner.getId();
        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertFalse(userRepository.existsById(id)),
                () -> assertTrue(roleRepository.existsById(role.getId()))
        );
    }

    @Test
    void shouldReturnNotFound_WhenDeleteUser_GivenInvalidID() {
        //given //when
        ResponseEntity<String> response = restTemplate
                .exchange(
                        "/api/users/{id}",
                        HttpMethod.DELETE,
                        null,
                        String.class,
                        100
                );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
