package pl.matkan.wholesaler.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matkan.wholesaler.auth.dto.JwtResponse;
import pl.matkan.wholesaler.auth.dto.LoginRequest;
import pl.matkan.wholesaler.auth.dto.RefreshTokenRequest;
import pl.matkan.wholesaler.auth.dto.RefreshTokenResponse;
import pl.matkan.wholesaler.auth.jwt.JwtUtils;
import pl.matkan.wholesaler.auth.refreshtoken.RefreshToken;
import pl.matkan.wholesaler.auth.refreshtoken.RefreshTokenRepository;
import pl.matkan.wholesaler.auth.refreshtoken.RefreshTokenService;
import pl.matkan.wholesaler.exception.RefreshTokenException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticate(loginRequest);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(jwtResponse.id());

        ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshTokenCookie(refreshToken.getToken());


        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(jwtResponse);

    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<RefreshTokenResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

        //TODO
        if ((refreshToken != null) && (!refreshToken.isEmpty())) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {

                        refreshTokenService.deleteByUserId(user.getId());
                        final RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
                        ResponseCookie jwtCookie = jwtUtils.generateRefreshTokenCookie(newRefreshToken.getToken());
                        final String newAccessToken = jwtUtils.generateJwtToken(user.getUsername());

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new RefreshTokenResponse(newAccessToken));
                    })
                    .orElseThrow(() -> new RefreshTokenException(refreshToken,
                            "Refresh token is not in database!"));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Refresh Token is empty!"));
    }
}
}
