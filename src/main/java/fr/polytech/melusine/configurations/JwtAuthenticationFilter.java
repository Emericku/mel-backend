package fr.polytech.melusine.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.polytech.melusine.components.TokenManager;
import fr.polytech.melusine.exceptions.errors.AuthorizationError;
import fr.polytech.melusine.exceptions.errors.ErrorMessage;
import fr.polytech.melusine.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * This filter is executed before all requests to extract the authentication from the headers.
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHENTICATION_HEADER_PREFIX = "Bearer ";

    private final AuthenticationService authenticationService;
    private final TokenManager tokenManager;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(AuthenticationService authenticationService, TokenManager tokenManager, ObjectMapper objectMapper) {
        this.authenticationService = authenticationService;
        this.tokenManager = tokenManager;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            extractJwtToken(request).ifPresent(this::setupAuthentication);
            filterChain.doFilter(request, response);
        } catch (SecurityException e) {
            log.error("Could not authenticate user", e);
            // This error happens if the given JWT token is invalid
            respondWithUnauthorizedError(request, response, AuthorizationError.JWT_INVALID);
        } catch (ExpiredJwtException e) {
            log.error("Could not authenticate user", e);
            // This error happens if the given JWT token has expired
            respondWithUnauthorizedError(request, response, AuthorizationError.JWT_EXPIRED);
        } finally {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }

    private Optional<String> extractJwtToken(HttpServletRequest request) {
        String parameterName = tokenManager.getJwtProperties().getHeaderName();

        Optional<String> jwtTokenFromHeader = findTokenInHeader(request, parameterName);
        if (jwtTokenFromHeader.isPresent()) {
            return jwtTokenFromHeader;
        }

        Optional<String> jwtTokenFromQueryParameter = findTokenInQueryParameter(request, parameterName.toLowerCase());
        if (jwtTokenFromQueryParameter.isPresent()) {
            return jwtTokenFromQueryParameter;
        }

        return findTokenInCookie(request, parameterName);
    }

    private Optional<String> findTokenInHeader(HttpServletRequest request, String parameterName) {
        String authHeaderValue = request.getHeader(parameterName);
        if (authHeaderValue != null && !authHeaderValue.trim().isEmpty()) {
            if (!authHeaderValue.toLowerCase().startsWith(AUTHENTICATION_HEADER_PREFIX.toLowerCase())) {
                throw new SecurityException("Invalid authentication header value: " + authHeaderValue);
            }
            log.trace("Found authentication header: {}", parameterName);
            return Optional.of(authHeaderValue.trim().substring(AUTHENTICATION_HEADER_PREFIX.length()));
        }

        log.trace("No authentication header found with name: {}", parameterName);
        return Optional.empty();
    }

    private Optional<String> findTokenInQueryParameter(HttpServletRequest request, String parameterName) {
        String authValue = request.getParameter(parameterName);
        if (authValue != null && !authValue.trim().isEmpty()) {
            log.trace("Found authentication parameter: {}", parameterName);
            return Optional.of(authValue.trim());
        }

        log.trace("No query parameter found with name: {}", parameterName);
        return Optional.empty();
    }

    private Optional<String> findTokenInCookie(HttpServletRequest request, String parameterName) {
        String authValue = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> cookie.getName().equals(parameterName))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
        if (authValue != null && !authValue.trim().isEmpty()) {
            log.trace("Found authentication cookie: {}", parameterName);
            return Optional.of(authValue.trim());
        }

        log.trace("No cookie found with name: {}", parameterName);
        return Optional.empty();
    }

    private void setupAuthentication(String jwtToken) {
        log.trace("Authenticating user with JWT token from header");
        Claims claims = tokenManager.parseAndValidateJwtToken(jwtToken);
        Authentication authenticationToken = authenticationService.withToken(claims, jwtToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void respondWithUnauthorizedError(HttpServletRequest request, HttpServletResponse response, AuthorizationError error) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setCode(error.getCode());
        errorMessage.setDescription(error.getDescription());
        objectMapper.writeValue(response.getOutputStream(), errorMessage);
    }

}
