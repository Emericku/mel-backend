package fr.polytech.melusine.components;

import fr.polytech.melusine.models.entities.Account;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class AuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -1293860848377162200L;

    private final Object user;
    private final String sessionRandom;
    private final String token;

    public AuthenticationToken(Account account, List<String> roles, String sessionRandom, String token) {
        super(roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
        this.user = account;
        this.sessionRandom = sessionRandom;
        this.token = token;
        setAuthenticated(true);
    }

    public String getSessionRandom() {
        return sessionRandom;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

}