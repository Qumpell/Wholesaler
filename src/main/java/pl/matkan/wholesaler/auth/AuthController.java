package pl.matkan.wholesaler.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matkan.wholesaler.auth.jwt.JwtResponse;
import pl.matkan.wholesaler.auth.refreshtoken.RefreshTokenResponse;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        JwtResponse jwtResponse = authService.authenticate(loginRequest, response);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        authService.logout(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/refreshtoken")
    public ResponseEntity<RefreshTokenResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        RefreshTokenResponse refreshTokenResponse = authService.refreshToken(request, response);
        return new ResponseEntity<>(refreshTokenResponse, HttpStatus.OK);
    }
}
