package fr.polytech.melusine.exceptions;

import fr.polytech.melusine.exceptions.errors.ErrorCode;

/**
 * This class defines a base exception with an error code.
 */
public abstract class ErrorCodeException extends RuntimeException {

    private final ErrorCode errorCode;

    public ErrorCodeException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public ErrorCodeException(ErrorCode errorCode, Object... args) {
        super(String.format(errorCode.getDescription(), args));
        this.errorCode = errorCode;
    }

    public ErrorCodeException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDescription(), cause);
        this.errorCode = errorCode;
    }

    public ErrorCodeException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(String.format(errorCode.getDescription(), args), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
