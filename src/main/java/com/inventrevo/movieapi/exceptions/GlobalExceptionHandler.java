package com.inventrevo.movieapi.exceptions;

import com.inventrevo.movieapi.dto.APIErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<Object> handleStudentNotFoundException(UsernameNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage(), null));
    }
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getLocalizedMessage(), null));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new APIErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), null)
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        return new ResponseEntity<>(new APIErrorResponse(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(), null), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, WebRequest request) {
        return new ResponseEntity<>(new APIErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        return new ResponseEntity(new APIErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new APIErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        return new ResponseEntity<>(new APIErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        return new ResponseEntity<>(new APIErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(),
                new ArrayList<>(List.of(error))),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        return new ResponseEntity<>(new APIErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(),
                new ArrayList<>(List.of(error))),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({HttpClientErrorException.Forbidden.class, ServletException.class, IOException.class})
    public ResponseEntity<Object> handleForbiddenException(HttpClientErrorException.Forbidden ex, WebRequest request) {
        return new ResponseEntity<>(new APIErrorResponse(HttpStatus.FORBIDDEN, ex.getLocalizedMessage(), null), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException e) {
        return new ResponseEntity<>(new APIErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
