package fr.polytech.melusine.exceptions.errors;

public enum OrderError implements ErrorCode {

    INVALID_ORDER_UUID("OR0001", "The order with the uuid: %s doesn't exists");

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
