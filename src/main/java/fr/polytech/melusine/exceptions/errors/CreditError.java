package fr.polytech.melusine.exceptions.errors;

public enum CreditError implements ErrorCode {

    INVALID_CREDIT("CR0001", "The credit is under 0, you trying to add: %s");

    private final String code;
    private final String description;

    CreditError(String code, String description) {
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
        return "CreditBusinessError{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
