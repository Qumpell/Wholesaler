package pl.matkan.wholesaler.auth;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.auth.dto.JwtResponse;
import pl.matkan.wholesaler.auth.dto.LoginRequest;
import pl.matkan.wholesaler.auth.dto.RefreshTokenResponse;
import pl.matkan.wholesaler.auth.jwt.JwtUtils;
import pl.matkan.wholesaler.auth.refreshtoken.RefreshToken;
import pl.matkan.wholesaler.auth.refreshtoken.RefreshTokenService;
import pl.matkan.wholesaler.exception.RefreshTokenException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    public JwtResponse authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        final String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());



        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();


        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        );

    }

    public RefreshTokenResponse refreshToken(String oldRefreshToken) {

        return refreshTokenService.findByToken(oldRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateJwtToken(user.getUsername());
                    return ResponseEntity.ok(new RefreshTokenResponse(token, ));
                })
                .orElseThrow(() -> new RefreshTokenException(oldRefreshToken,
                        "Refresh token is not in database!"));
    }
    }
}
