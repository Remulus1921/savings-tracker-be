package org.bekierz.savingstrackerbe.utils.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.bekierz.savingstrackerbe.user.model.CustomUserDetails;
import org.bekierz.savingstrackerbe.utils.config.EnvironmentVariables;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.function.Function;

@Service
@Log4j2
public class JwtService {

    private final EnvironmentVariables environmentVariables;

    public JwtService(EnvironmentVariables environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public String extractEmail(String token) {
        return extractSingleClaim(token, Claims::getSubject);
    }

    public <T> T extractSingleClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(CustomUserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            HashMap<String, Object> extraClaims,
            CustomUserDetails userDetails
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(java.sql.Date.valueOf(LocalDate.now().plusDays(30)))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token, CustomUserDetails userDetails) {
        final String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractSingleClaim(token, Claims::getExpiration);
    }

    private Claims extractClaims(String jwtToken) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(environmentVariables.getJwtSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
