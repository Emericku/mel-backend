package fr.polytech.melusine.exceptions.errors;

public enum IngredientError implements ErrorCode {

    INVALID_INGREDIENT_NAME("ON0001", "The ingredient with the name: %s doesn't exists"),
    INVALID_INGREDIENT_UUID("ON0001", "The ingredient with the ingredient id: %s doesn't exists");

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
