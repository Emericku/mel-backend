package fr.polytech.melusine.exceptions.errors;

public enum AccountError implements ErrorCode {

    INVALID_CLIENT_ID("LO0001", "Account is not found with client ID: %s"),
    INVALID_EMAIL("LO0002", "Account is not found with email: %s"),
    CONFLICT_EMAIL("LO0003", "An account already exists with  not email: %s");

    private final String code;
    private final String description;

    AccountError(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "AccountBusinessError{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
