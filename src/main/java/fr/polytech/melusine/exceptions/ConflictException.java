package fr.polytech.melusine.exceptions;

import fr.polytech.melusine.exceptions.errors.ErrorCode;

public class ConflictException extends ErrorCodeException {

    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ConflictException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
