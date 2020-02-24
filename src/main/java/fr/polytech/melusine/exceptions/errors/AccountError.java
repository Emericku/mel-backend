package fr.polytech.melusine.exceptions.errors;

public enum AccountError implements ErrorCode {

    INVALID_CLIENT_ID("AC0001", "Le compte avec l'ID : %s est introuvable"),
    INVALID_EMAIL("AC0002", "Le compte avec l'email : %s est introuvable"),
    CONFLICT_EMAIL("AC0003", "Le compte avec l'email : %s est déjà existant");

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
