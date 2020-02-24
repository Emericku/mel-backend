package fr.polytech.melusine.exceptions.errors;

public enum SystemError implements ErrorCode {

    TECHNICAL_ERROR("SY0001", "Erreur technique : %s");

    private final String code;
    private final String description;

    SystemError(String code, String description) {
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
        return "SystemError{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
