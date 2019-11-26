package fr.polytech.melusine.exceptions.errors;

import lombok.Value;

import java.util.Map;

@Value
public class ValidationError {

    public static final String SIZE = "VA0001";
    public static final String SIZE_PARAM_MAX = "max";
    public static final String SIZE_PARAM_MIN = "min";
    public static final String PASSWORD = "VA0002";
    public static final String NOT_NULL = "VA0003";
    public static final String NOT_EMPTY = "VA0004";
    public static final String PATTERN = "VA0005";
    public static final String NOT_NULL_IF_ANOTHER_FIELD_IS_FALSE = "VA0006";
    public static final String NUMBER = "VA0007";

    private String code;

    private String message;

    private Map<String, Object> params;

}
