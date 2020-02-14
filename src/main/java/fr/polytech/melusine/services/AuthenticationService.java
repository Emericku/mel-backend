package fr.polytech.melusine.services;

import fr.polytech.melusine.components.AuthenticationToken;
import fr.polytech.melusine.components.TokenManager;
import fr.polytech.melusine.exceptions.NotFoundException;
import fr.polytech.melusine.exceptions.UnauthorizedException;
import fr.polytech.melusine.exceptions.errors.AccountError;
import fr.polytech.melusine.exceptions.errors.AuthenticationError;
import fr.polytech.melusine.models.dtos.responses.AuthenticationResponse;
import fr.polytech.melusine.models.entities.Account;
import fr.polytech.melusine.repositories.AccountRepository;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AuthenticationService {

    private static final String BEARER = "bearer";
    private static final String CLAIM_SESSION_RANDOM = "sessionRandom";

    private AccountRepository accountRepository;
    private PasswordService passwordService;
    private TokenManager tokenManager;

    public AuthenticationService(AccountRepository accountRepository, PasswordService passwordService,
                                 TokenManager tokenManager) {
        this.accountRepository = accountRepository;
        this.passwordService = passwordService;
        this.tokenManager = tokenManager;
    }

    /**
     * Authenticate a user with password.
     *
     * @param email
     * @param password
     * @return
     */
    public AuthenticationResponse withPassword(String email, String password) {
        log.debug("Authentication with username: {}", email);
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(AccountError.INVALID_EMAIL, email));

        boolean isMatching = passwordService.passwordsMatch(password, account.getPassword());

        if (!isMatching) {
            throw new UnauthorizedException(AuthenticationError.INVALID_CREDENTIALS);
        }
        return AuthenticationResponse.builder()
                .accessToken(tokenManager.generateJWT(account))
                .tokenType(BEARER)
                .expiresIn(tokenManager.getJwtProperties().getTimeToLive())
                .build();
    }

    /**
     * Authenticate an user in Spring security using a JWT token.
     *
     * @param claims the claims of the JWT token
     * @param token  the original token
     * @return a Spring Authentication
     * @throws AuthenticationException if the user cannot be authenticated
     */
    public Authentication withToken(Claims claims, String token) throws AuthenticationException {
        log.trace("Looking up for a user with subject: {}", claims.getSubject());
        Account account = accountRepository.findById(claims.getSubject())
                .orElseThrow(() -> new BadCredentialsException("Unknown user <" + claims.getSubject() + ">"));

        log.debug("User: {} successfully authenticated", claims.getSubject());
        String sessionRandom = claims.get(CLAIM_SESSION_RANDOM, String.class);
        return new AuthenticationToken(account, List.of(), sessionRandom, token);
    }

}
