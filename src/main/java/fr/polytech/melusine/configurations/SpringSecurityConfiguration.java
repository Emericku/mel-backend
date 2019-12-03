package fr.polytech.melusine.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.polytech.melusine.components.TokenManager;
import fr.polytech.melusine.exceptions.errors.AuthorizationError;
import fr.polytech.melusine.exceptions.errors.ErrorCode;
import fr.polytech.melusine.exceptions.errors.ErrorMessage;
import fr.polytech.melusine.services.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * This class contains all security related configurations.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOCAL_PROFILE = "LOCAL";

    private final Environment environment;
    private final AuthenticationService authenticationService;
    private final TokenManager tokenManager;
    private final ObjectMapper objectMapper;

    @Autowired
    public SpringSecurityConfiguration(Environment environment, AuthenticationService authenticationService, TokenManager tokenManager, ObjectMapper objectMapper) {
        this.environment = environment;
        this.authenticationService = authenticationService;
        this.tokenManager = tokenManager;
        this.objectMapper = objectMapper;
    }

    /**
     * Configure web security for the API endpoints.
     *
     * @param http the security builder
     * @throws Exception if the configuration could not be applied
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> profiles = Arrays.asList(environment.getActiveProfiles());

        if (profiles.isEmpty() || profiles.contains(LOCAL_PROFILE)) http.headers().frameOptions().disable();
        else
            http.headers().frameOptions().sameOrigin();

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()

                // Allow access to token distribution and validation endpoints
                .antMatchers(HttpMethod.POST, "/auth/token").permitAll()

                // Allow access to the Swagger documentation
                .antMatchers(
                        "/webjars/**",
                        "/configuration/ui",
                        "/configuration/security"
                ).permitAll()

                // Allow CORS OPTION methods
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .antMatchers(HttpMethod.GET, "/favicon.ico").permitAll()

                .antMatchers(HttpMethod.POST, "/users/register").permitAll()

                // All other requests need to be authenticated
                .anyRequest().authenticated().and()

                // Custom JSON based authentication based on the header previously given to the client
                .addFilterBefore(new JwtAuthenticationFilter(authenticationService, tokenManager, objectMapper), BasicAuthenticationFilter.class)

                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint);
    }

    private final AuthenticationEntryPoint authEntryPoint = new AuthenticationEntryPoint() {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            log.error("Error authenticating user", authException);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            // test that header is not null else an error will be thrown (in integration tests we don't have an ORIGIN header)
            if (request.getHeader(HttpHeaders.ORIGIN) != null)
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ErrorMessage errorMessage;
            // This error happens if the user from the JWT token cannot be found by AuthenticationService#authenticateWithToken
            // This error happens if there is no token in the JwtProperties#getHeaderName header
            if (authException instanceof BadCredentialsException)
                errorMessage = buildErrorMessage(AuthorizationError.JWT_INCOHERENT);
            else
                errorMessage = buildErrorMessage(AuthorizationError.JWT_INVALID);
            objectMapper.writeValue(response.getOutputStream(), errorMessage);
        }

        private ErrorMessage buildErrorMessage(ErrorCode errorCode) {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setCode(errorCode.getCode());
            errorMessage.setDescription(errorCode.getDescription());
            return errorMessage;
        }

    };

    /**
     * Enable CORS from all origins.
     *
     * @return a CorsFilter bean
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(Boolean.TRUE);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Content-Disposition");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
