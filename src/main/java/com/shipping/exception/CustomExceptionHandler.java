package com.shipping.exception;

import com.shipping.model.ErrorResponse;
import com.shipping.util.ShippingConstants;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {


    @Autowired
    private MessageSource messageSource;


    @Override
    public final ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        List<String> details =  ex.getFieldErrors()
                .parallelStream()
                .map(e -> messageSource.getMessage(e.getDefaultMessage(),null, Locale.ENGLISH))
                .collect(Collectors.toList());
        ErrorResponse error = new ErrorResponse(ShippingConstants.ERROR_CODE_BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handle(BadCredentialsException ex, HttpServletRequest request, HttpServletResponse response) {

        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        ErrorResponse error = new ErrorResponse(ShippingConstants.ERROR_CODE_BAD_CREDENTIALS, details);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDBIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request, HttpServletResponse response) {

        List<String> details = new ArrayList<>();
        details.add(messageSource.getMessage(ShippingConstants.ERROR_MESSAGE_DUPLICATE_RECORD_FOUND,null,Locale.ENGLISH));
        ErrorResponse error = new ErrorResponse(ShippingConstants.ERROR_MESSAGE_DUPLICATE_RECORD_FOUND, details);
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<Object> handleNoRecords(NoResultException ex, HttpServletRequest request, HttpServletResponse response) {

        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        ErrorResponse  error = new ErrorResponse(ShippingConstants.ERROR_CODE_NO_RECORD_FOUND, details);
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericError(Exception ex, HttpServletRequest request, HttpServletResponse response) {

        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        details.add(messageSource.getMessage(ShippingConstants.ERROR_MESSAGE_INTERNAL_SERVER,null,Locale.ENGLISH));
        ErrorResponse  error = new ErrorResponse(ShippingConstants.ERROR_CODE_INTERNAL_SERVER, details);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
