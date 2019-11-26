package fr.polytech.melusine.exceptions;

import fr.polytech.melusine.exceptions.errors.ErrorCode;

public class BadRequestException extends ErrorCodeException {

    public BadRequestException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
