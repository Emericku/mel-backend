package fr.polytech.melusine.exceptions.errors;

public enum ProductError implements ErrorCode {

    INVALID_NAME("PR0001", "Le produit avec le nom : %s est introuvable"),
    NOT_FOUND("PR0002", "Le produit avec l'ID : %s  est introuvable"),
    CONFLICT("PR0003", "Le produit avec le nom : %s existe déjà");

    private final String code;
    private final String description;

    ProductError(String code, String description) {
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
