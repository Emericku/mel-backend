package fr.polytech.melusine.exceptions.errors;

public enum AuthorizationError implements ErrorCode {

    NOT_AUTHORIZED("A0001", "User is not authorized."),
    JWT_INCOHERENT("A0002", "The JWT token is valid but the referenced user is not known in the system"),
    JWT_INVALID("A0003", "The JWT token is missing or an invalid token was provided"),
    JWT_EXPIRED("A0004", "The JWT token is expired");

    private final String code;
    private final String description;

    AuthorizationError(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

}
