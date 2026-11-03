package com.rideflow.auth_service.security;

import com.rideflow.auth_service.domain.Credential;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtTokenService {

    private final long expiryInSeconds;
    private final SecretKey signingKey;
    private final String issuer;

    public JwtTokenService(
            @Value("${rideflow.jwt.expiry-seconds}") long expiryInSeconds,
            @Value("${rideflow.jwt.secret}") String secret,
            @Value("${rideflow.jwt.issuer}") String issuer) {
        this.expiryInSeconds = expiryInSeconds;
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
    }

    public String issueToken(Credential credential) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expiryInSeconds);
        return Jwts.builder()
                .issuer(issuer)
                .subject(credential.getSubjectId().toString())
                .claim("email", credential.getEmail())
                .claim("role", credential.getRole())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(signingKey)
                .compact();
    }

    public long getExpiryInSeconds() {
        return expiryInSeconds;
    }
}