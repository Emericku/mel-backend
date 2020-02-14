package fr.polytech.melusine.exceptions.errors;

public enum ProductError implements ErrorCode {

    INVALID_NAME("PR0001", "The product with the name: %s doesn't exists"),
    NOT_FOUND("PR0002", "The product with the id: %s not found"),
    CONFLICT("PR0003", "Product already exists with name: %s");

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
