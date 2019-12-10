package fr.polytech.melusine.exceptions.errors;

public enum OrderError implements ErrorCode {

    INVALID_ORDER_UUID("OR0001", "The order with the uuid: %s doesn't exists"),
    INVALID_ORDER("OR0002", "The order has no products"),
    ORDER_NOT_FOUND("OR0003", "The order with id %s is not found"),
    ORDER_ITEM_NOT_FOUND("OR0004", "The order item with id %s is not found"),
    ORDER_ALREADY_DELIVERED("OR0005", "The order with id : %s is already delivered"),
    ORDER_ITEM_ALREADY_DELIVERED("OR0006", "The order item with id : %s is already delivered"),
    ORDER_ITEM_ALREADY_CANCELLED("OR0007", "The order item with id : %s is already cancelled");

    private final String code;
    private final String description;

    OrderError(String code, String description) {
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
        return "OrderBusinessError{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
