package fr.polytech.melusine.exceptions.errors;

public enum UserError implements ErrorCode {

    NOT_FOUND("US0001", "User is not found with ID : %s"),
    CONFLICT("US0002", "User already exists with first name : %s, last name : %s and section : %s");

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
