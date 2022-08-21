package com.comic.files.handler;

import com.comic.files.dto.DErrorResponse;
import com.comic.files.exception.InvalidInsertException;
import com.comic.files.exception.NotFoundObjectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidInsertException.class)
    public ResponseEntity<DErrorResponse> invalidInsertException(InvalidInsertException e) {
        return handleException(Objects.nonNull(e.getId())?"Failed to insert object":"Failed to update object",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundObjectException.class)
    public ResponseEntity<DErrorResponse> notFoundObjectException(NotFoundObjectException e) {
        return handleException(e.getObject()+" with ID: "+e.getId()+" not found",HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DErrorResponse> generalException(Exception e) {
        return handleException(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<DErrorResponse> handleException(String message, HttpStatus status) {
        return new ResponseEntity<>(new DErrorResponse(message),status);
    }
}
