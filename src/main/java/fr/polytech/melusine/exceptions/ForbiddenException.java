package fr.polytech.melusine.exceptions;

import fr.polytech.melusine.exceptions.errors.ErrorCode;

public class ForbiddenException extends ErrorCodeException {

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ForbiddenException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
