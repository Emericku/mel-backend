package fr.polytech.melusine.components;

import fr.polytech.melusine.configurations.JwtProperties;
import fr.polytech.melusine.models.entities.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Date;

@Slf4j
@Component
@EnableConfigurationProperties({JwtProperties.class})
public class TokenManager {

    private static final String CLAIM_EMAIL = "email";

    private final JwtProperties jwtProperties;
    private final Clock clock;

    public TokenManager(JwtProperties jwtProperties, Clock clock) {
        this.jwtProperties = jwtProperties;
        this.clock = clock;
    }

    /**
     * Generate the JWT token.
     *
     * @param account the connection account
     * @return
     */
    public String generateJWT(Account account) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSignKey())
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(Date.from(clock.instant()))
                .setExpiration(Date.from(clock.instant().plusSeconds(jwtProperties.getTimeToLive())))
                .setSubject(account.getId())
                .claim(CLAIM_EMAIL, account.getEmail())
                .compact();
    }

    /**
     * Return all JWT properties of the application.
     *
     * @return the JWT properties
     */
    public JwtProperties getJwtProperties() {
        return jwtProperties;
    }

    /**
     * Extracts the content of a JWT token.
     *
     * @param token the token as a Base64 encoded string
     * @return the parsed token
     */
    public Claims parseAndValidateJwtToken(String token) {
        log.trace("Parsing JWT token " + token.substring(0, Math.min(token.length(), 10)) + "[...]");
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(jwtProperties.getSignKey())
                .parseClaimsJws(token);
        Claims claims = jws.getBody();

        log.trace("JWT token content: " + claims.toString());
        assertClaimPresent(claims, Claims.ISSUER);
        assertClaimPresent(claims, Claims.ISSUED_AT);
        assertClaimPresent(claims, Claims.EXPIRATION);
        assertClaimPresent(claims, Claims.SUBJECT);
        assertClaimPresent(claims, CLAIM_EMAIL);

        return claims;
    }

    private void assertClaimPresent(Claims claims, String key) {
        if (claims.get(key) == null) {
            throw new SecurityException("JWT token does not contain claim <" + key + ">");
        }
    }

}
