package fr.polytech.melusine.configurations.handlers;

import fr.polytech.melusine.exceptions.*;
import fr.polytech.melusine.exceptions.errors.AuthorizationError;
import fr.polytech.melusine.exceptions.errors.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage handleBadRequestException(BadRequestException ex, WebRequest request, HttpServletResponse response) {
        log.error("BadRequestException in controller", ex);
        return buildErrorMessageWithErrorCodeException(ex);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorMessage handleUnauthorizedException(UnauthorizedException ex, WebRequest request, HttpServletResponse response) {
        log.error("UnauthorizedException in controller", ex);
        return buildErrorMessageWithErrorCodeException(ex);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorMessage handleForbiddenException(ForbiddenException ex, WebRequest request, HttpServletResponse response) {
        log.error("ForbiddenException in controller", ex);
        return buildErrorMessageWithErrorCodeException(ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorMessage handleAccessDeniedException(AccessDeniedException ex, WebRequest request, HttpServletResponse response) {
        log.error("AccessDeniedException in controller", ex);
        return buildErrorMessageWithErrorCode(AuthorizationError.NOT_AUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessage handleNotFoundException(NotFoundException ex, WebRequest request, HttpServletResponse response) {
        log.error("NotFoundException in controller", ex);
        return buildErrorMessageWithErrorCodeException(ex);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorMessage handleConflictException(ConflictException ex, WebRequest request, HttpServletResponse response) {
        log.error("ConflictException in controller", ex);
        return buildErrorMessageWithErrorCodeException(ex);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage handleInternalServerErrorException(InternalServerErrorException ex, WebRequest request, HttpServletResponse response) {
        log.error("InternalServerErrorException in controller", ex);
        return buildErrorMessageWithErrorCodeException(ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorMessage handleRuntimeException(Exception ex, WebRequest request, HttpServletResponse response) {
        log.error("Unhandled RuntimeException in controller", ex);
        return buildErrorMessageWithStatusAndException(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

}
