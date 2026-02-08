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
import pl.matkan.wholesaler.role.Role;
import pl.matkan.wholesaler.role.RoleRepository;
import pl.matkan.wholesaler.role.RoleRequest;
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
public class RoleControllerTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.0.1");

    @LocalServerPort
    int randomServerPort;

    @Autowired
    TestRestTemplate restTemplate;

    RestClient restClient;
    
    @Autowired
    private RoleRepository roleRepository;

    private Role role;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + randomServerPort);

        role = new Role(null, "admin", new HashSet<>());
        role = roleRepository.save(role);
        
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
    void shouldFindAllRoles() {

        //when
        ResponseEntity<RestPageImpl<Role>> responseEntity = restClient
                .get()
                .uri("/api/roles")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });

        RestPageImpl<Role> body = responseEntity.getBody();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(1, Objects.requireNonNull(body).getTotalElements())
        );
    }


    @Test
    void shouldGetOneRole_GivenValidId() {
        //given
        //when
        ResponseEntity<Role> responseEntity = restClient.get()
                .uri("/api/roles/{id}", role.getId())
                .retrieve()
                .toEntity(Role.class);

        Role responseEntityBody = responseEntity.getBody();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(role.getId(), Objects.requireNonNull(responseEntityBody).getId()),
                () -> assertEquals(role.getName(), Objects.requireNonNull(responseEntityBody).getName())
        );

    }

    @Test
    void shouldReturnNotFound_WhenGetOneRole_GivenInvalidId() {

        //when
        ResponseEntity<Role> responseEntity = restTemplate.
                exchange("/api/roles/100",
                        HttpMethod.GET,
                        null,
                        Role.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnConflict_WhenCreateRole_WithAlreadyExistingRolename() {

        //given
        RoleRequest roleRequest = new RoleRequest(
           role.getName()
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.
                postForEntity(
                        "/api/roles",
                        roleRequest,
                        String.class
                );

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldCreateRole_GivenValidData() {

        //given
        RoleRequest roleRequest = new RoleRequest(
             "USER"
        );

        //when
        ResponseEntity<Role> responseEntity = restClient.post()
                .uri("/api/roles")
                .contentType(APPLICATION_JSON)
                .body(roleRequest)
                .retrieve()
                .toEntity(Role.class);

        Role responseEntityBody = responseEntity.getBody();

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                () -> assertEquals(roleRequest.name(), Objects.requireNonNull(responseEntityBody).getName())
              
        );
    }

    @Test
    void shouldUpdateRole() {

        //given
        RoleRequest roleRequest = new RoleRequest(
                "ADMIN1"
        );

        //when
        ResponseEntity<Role> responseEntity = restClient
                .put()
                .uri("/api/roles/{id}", role.getId())
                .contentType(APPLICATION_JSON)
                .body(roleRequest)
                .retrieve()
                .toEntity(Role.class);


        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(roleRequest.name(), Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).getName()))
        );
    }

    @Test
    void shouldReturnNotFound_WhenUpdateRole_GivenInvalidId() {

        //given
        RoleRequest roleRequest = new RoleRequest(
               "ADMIN1"
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(
                        "/api/roles/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(roleRequest),
                        String.class,
                        100
                );


        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotDeleteRelatedUsers_WhenDeleteRole() {

        //given
        User user1 = new User(
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


        role.getUsers().add(user1);
        userRepository.save(user1);
        roleRepository.save(role);
        //when
        ResponseEntity<Role> responseEntity = restClient
                .delete()
                .uri("/api/roles/{id}", role.getId())
                .retrieve()
                .toEntity(Role.class);

        List<User> users = userRepository.findAll();

        //then
        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode()),
                () -> assertEquals(1, users.size())
        );
    }

    @Test
    void shouldDeleteRole() {
        //given
        //when
        ResponseEntity<String> response = restClient
                .delete()
                .uri("/api/roles/{id}", role.getId())
                .retrieve()
                .toEntity(String.class);

        //then
        final long id = role.getId();
        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertFalse(userRepository.existsById(id))
        );
    }

    @Test
    void shouldReturnNotFound_WhenDeleteRole_GivenInvalidID() {
        //given //when
        ResponseEntity<String> response = restTemplate
                .exchange(
                        "/api/roles/{id}",
                        HttpMethod.DELETE,
                        null,
                        String.class,
                        100
                );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
