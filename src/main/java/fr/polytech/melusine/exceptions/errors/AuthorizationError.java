package fr.polytech.melusine.exceptions.errors;

public enum AuthorizationError implements ErrorCode {

    NOT_AUTHORIZED("A0001", "L'utilisateur n'est pas authorisé."),
    JWT_INCOHERENT("A0002", "Le JWT est incoherent"),
    JWT_INVALID("A0003", "Le JWT est manquant ou invalide"),
    JWT_EXPIRED("A0004", "La connexion est expirée");

    private final String code;
    private final String description;

    AuthorizationError(String code, String description) {
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

}
