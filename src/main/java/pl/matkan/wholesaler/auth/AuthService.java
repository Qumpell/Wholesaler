package pl.matkan.wholesaler.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.matkan.wholesaler.auth.jwt.JwtResponse;
import pl.matkan.wholesaler.auth.refreshtoken.RefreshTokenResponse;
import pl.matkan.wholesaler.auth.jwt.JwtUtils;
import pl.matkan.wholesaler.auth.refreshtoken.RefreshToken;
import pl.matkan.wholesaler.auth.refreshtoken.RefreshTokenRepository;
import pl.matkan.wholesaler.auth.refreshtoken.RefreshTokenService;
import pl.matkan.wholesaler.exception.RefreshTokenException;
import pl.matkan.wholesaler.user.User;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtResponse authenticate(LoginRequest loginRequest, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        refreshTokenService.deleteByUserId(userDetails.getId());
        final RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        final ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshTokenCookie(refreshToken.getToken());
        response.addHeader(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString());

        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        );
    }

    public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        final String refreshTokenFromCookies = jwtUtils.getJwtRefreshFromCookies(request);
        if ((refreshTokenFromCookies != null) && (!refreshTokenFromCookies.isEmpty())) {
            RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenFromCookies)
                    .orElseThrow(() -> new RefreshTokenException(refreshTokenFromCookies, "Refresh token is not in database!"));

            refreshTokenService.verifyExpiration(refreshToken);
            final User user = refreshToken.getUser();
            refreshTokenRepository.delete(refreshToken);
            final String newAccessToken = jwtUtils.generateJwtToken(user.getUsername());
            final RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
            final ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshTokenCookie(newRefreshToken.getToken());

            response.addHeader(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString());

            return new RefreshTokenResponse(newAccessToken);

        }
        else{
            throw new RefreshTokenException(refreshTokenFromCookies, "Refresh token is not in database!");
        }
    }

    public void logout(HttpServletResponse response) {

        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!Objects.equals(principle.toString(), "anonymousUser")) {
            Long userId = ((UserDetailsImpl) principle).getId();
            refreshTokenService.deleteByUserId(userId);
        }

        final ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString());
    }
}

