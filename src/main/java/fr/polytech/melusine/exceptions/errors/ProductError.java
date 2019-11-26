package fr.polytech.melusine.exceptions.errors;

public enum ProductError implements ErrorCode {

    INVALID_PRODUCT_NAME("PR0001", "The product with the name: %s doesn't exists"),
    INVALID_PRODUCT_INGREDIENT("PR0002", "You try to add a custom product without ingredients"),
    INVALID_PRODUCT_TYPE("PR0003", "You can't create a custom product"),
    INVALID_PRODUCT_UUID("PR0004", "The product with the UUID: %s doesn't exists"),
    INVALID_PRODUCT_ITEM_ID("PR0005", "One of the item product you want to delete doesn't exists in the order");

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
