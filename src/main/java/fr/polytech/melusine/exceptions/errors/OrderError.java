package fr.polytech.melusine.exceptions.errors;

public enum OrderError implements ErrorCode {

    INVALID_ORDER("OR0001", "La commande n'a pas d'item"),
    ORDER_NOT_FOUND("OR0002", "La commande avec l'ID : %s est introuvable"),
    ORDER_ITEM_NOT_FOUND("OR0003", "L'item de commande avec l'ID : %s est introuvable"),
    ORDER_ITEM_IS_NOT_PENDING("OR0004", "TL'item de commande avec l'ID : %s est déjà livré ou annulé");

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
