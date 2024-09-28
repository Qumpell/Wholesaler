package pl.matkan.wholesaler.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matkan.wholesaler.auth.dto.JwtResponse;
import pl.matkan.wholesaler.auth.dto.LoginRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;


    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.authenticate(loginRequest), HttpStatus.OK);
    }
}
