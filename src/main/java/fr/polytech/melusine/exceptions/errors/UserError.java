package fr.polytech.melusine.exceptions.errors;

public enum UserError implements ErrorCode {

    NOT_FOUND("US0001", "L'utilisateur avec l'ID : %s est introuvable"),
    CONFLICT("US0002", "L'utilisateur avec le prénom: %s, et le nom: %s et la section : %s existe déjà"),
    USER_CREDIT_UNDER_ZERO("US0002", "L'utilsateur avec l'ID : %s a un solde égale ou inférieur à 0");

    private final String code;
    private final String description;

    UserError(String code, String description) {
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
        return "UserBusinessError{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}
