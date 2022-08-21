package com.comic.files.handler;

import com.comic.files.dto.DErrorResponse;
import com.comic.files.enums.EnumErrors;
import com.comic.files.exception.InvalidInsertException;
import com.comic.files.exception.NotFoundObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(InvalidInsertException.class)
    public ResponseEntity<DErrorResponse> invalidInsertException(InvalidInsertException e) {
        return handleException(Objects.nonNull(e.getId())?messageSource.getMessage(EnumErrors.ERROR_INSERT.getMessage(),null, Locale.getDefault()):messageSource.getMessage(EnumErrors.ERROR_UPDATE.getMessage(),null, Locale.getDefault()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundObjectException.class)
    public ResponseEntity<DErrorResponse> notFoundObjectException(NotFoundObjectException e) {
        return handleException(messageSource.getMessage(EnumErrors.ERROR_NOT_FOUND.getMessage(),new Object[]{e.getObject(),e.getId()}, Locale.getDefault()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DErrorResponse> generalException(Exception e) {
        return handleException(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<DErrorResponse> handleException(String message, HttpStatus status) {
        return new ResponseEntity<>(new DErrorResponse(message),status);
    }
}
