package fr.polytech.melusine.utils;

import fr.polytech.melusine.exceptions.ForbiddenException;
import fr.polytech.melusine.exceptions.errors.AccountError;
import fr.polytech.melusine.models.entities.Account;
import org.springframework.security.core.context.SecurityContextHolder;

public final class AuthenticatedFinder {

    private AuthenticatedFinder() {
    }

    public static Account getAuthenticatedUser() {
        return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static void ensureAuthenticatedUserIsAdmin() {
        if (!getAuthenticatedUser().isAdmin()) {
            throw new ForbiddenException(AccountError.INVALID_RIGHTS);
        }
    }

}
