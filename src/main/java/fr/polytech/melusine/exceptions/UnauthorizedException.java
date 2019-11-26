package fr.polytech.melusine.exceptions;

import fr.polytech.melusine.exceptions.errors.ErrorCode;

public class UnauthorizedException extends ErrorCodeException {

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
