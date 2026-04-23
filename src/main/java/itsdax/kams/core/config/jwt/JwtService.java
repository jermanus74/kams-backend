package itsdax.kams.core.config.jwt;

import itsdax.kams.core.config.exceptions.InvalidTokenException;
import itsdax.kams.features.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    // In-memory blacklist (use Redis in production)
    private final Map<String, Instant> blacklistedJtis = new ConcurrentHashMap<>();

    // =========================
    // TOKEN GENERATION
    // =========================
    public String generateToken(User user) {

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(60 * 60 * 24 * 7); // 7 days
        String jti = UUID.randomUUID().toString();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("kams-system")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getRegistrationNumber())
                .id(jti)
                .claim("role", user.getRole().name())
                .claim("userId", user.getId())
                .build();

        JwsHeader headers = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(headers, claims)
        ).getTokenValue();
    }

    // =========================
    // TOKEN VALIDATION (CLEAN)
    // =========================
    public Jwt validateToken(String token) {

        if (token == null || token.isBlank()) {
            throw new InvalidTokenException("MISSING_TOKEN");
        }

        Jwt jwt;

        try {
            jwt = jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new InvalidTokenException("INVALID_TOKEN");
        }

        // =========================
        // EXPIRATION CHECK
        // =========================
        if (jwt.getExpiresAt() == null || jwt.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidTokenException("TOKEN_EXPIRED");
        }

        // =========================
        // CLEAN BLACKLIST
        // =========================
        blacklistedJtis.entrySet().removeIf(
                entry -> entry.getValue().isBefore(Instant.now())
        );

        // =========================
        // BLACKLIST CHECK
        // =========================
        String jti = jwt.getId();
        if (jti != null && blacklistedJtis.containsKey(jti)) {
            throw new InvalidTokenException("TOKEN_REVOKED");
        }

        return jwt;
    }

    // =========================
    // TOKEN INVALIDATION (LOGOUT)
    // =========================
    public void invalidateToken(String token) {

        if (token == null || token.isBlank()) return;

        try {
            Jwt jwt = jwtDecoder.decode(token);

            String jti = jwt.getId();
            Instant expiresAt = jwt.getExpiresAt();

            if (jti != null && expiresAt != null) {
                blacklistedJtis.put(jti, expiresAt);
            }

        } catch (JwtException e) {
            // silently ignore invalid token
        }
    }

    // =========================
    // EXTRACTORS
    // =========================
    public String extractRegistrationNumber(Jwt jwt) {
        return jwt.getSubject();
    }

    public String extractRole(Jwt jwt) {
        return jwt.getClaim("role");
    }

    public Long extractUserId(Jwt jwt) {
        return jwt.getClaim("userId");
    }
}