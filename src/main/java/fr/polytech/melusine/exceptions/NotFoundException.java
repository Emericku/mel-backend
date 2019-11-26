package fr.polytech.melusine.exceptions;

import fr.polytech.melusine.exceptions.errors.ErrorCode;

public class NotFoundException extends ErrorCodeException {

    public NotFoundException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
