package fr.polytech.melusine.exceptions.errors;

/**
 * This class is used to serialize error messages as JSON objects.
 */
public class ErrorMessage {

    private String code;
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
