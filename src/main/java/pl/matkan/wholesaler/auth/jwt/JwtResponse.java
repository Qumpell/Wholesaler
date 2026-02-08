package pl.matkan.wholesaler.auth.jwt;

import java.util.List;

public record JwtResponse(
        String accessToken,
        Long id,
        String username,
        String email,
        List<String> roles
) {
}
