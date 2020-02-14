package fr.polytech.melusine.exceptions.errors;

public enum IngredientError implements ErrorCode {

    INVALID_INGREDIENT_NAME("IN0001", "The ingredient with the name: %s doesn't exists"),
    NOT_FOUND("IN0002", "The ingredient with the id: %s not found"),
    CONFLICT("IN0003", "Ingredient already exists with name: %s");

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
