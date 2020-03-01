package fr.polytech.melusine.exceptions.errors;

public enum IngredientError implements ErrorCode {

    NOT_FOUND("IN0001", "L'ingredient avec l'ID : %s est introuvable"),
    DELETED("IN0002", "L'ingredient avec l'ID : %s est supprimé"),
    NOT_POSSIBLE_TO_DELETE_PAIN("IN0003", "Suppression de l'ingredient Pain impossible");
    private final String code;
    private final String description;

    IngredientError(String code, String description) {
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
        return "ProductBusinessError{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
