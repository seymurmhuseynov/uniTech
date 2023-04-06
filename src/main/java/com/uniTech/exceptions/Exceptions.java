package com.uniTech.exceptions;

import com.uniTech.models.Response;
import com.uniTech.models.ResponseData;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class Exceptions extends Throwable {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotFoundException.class)
    public Response notFoundException() {
        return new Response().setCode(HttpStatus.NOT_FOUND.value()).setMessage("Data Not Found");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AlreadyExistException.class)
    public Response alreadyExistException() {
        return new Response().setCode(HttpStatus.ALREADY_REPORTED.value()).setMessage("Data Already Exist");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AccessDeniedException.class)
    public Response accessDeniedException(AccessDeniedException e) {
        return new Response().setCode(HttpStatus.UNAUTHORIZED.value()).setMessage("Access Denied");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public Response constraintViolationException(ConstraintViolationException e) {
        return new Response().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Response dataIntegrityViolationException(DataIntegrityViolationException e) {
        return new Response().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public Response invalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException e) {
        return new Response().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(JpaSystemException.class)
    public Response jpaSystemException(JpaSystemException e) {
        return new Response().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage(e.getMessage());
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new Response().setCode(HttpStatus.BAD_REQUEST.value()).setMessage(e.getMessage());
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new Response().setCode(HttpStatus.BAD_REQUEST.value()).setMessage(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Response methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return new Response().setCode(HttpStatus.BAD_REQUEST.value()).setMessage(e.getMessage());
    }
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseData<?> methodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ResponseEntity.ok("");
        return ResponseData.error(errors, HttpStatus.NOT_ACCEPTABLE);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotAcceptable.class)
    public ResponseData<?> notAcceptable(NotAcceptable e){
        String message = e.getMessage();
        if(message == null || message.length() == 0){
            message = "Not Acceptable";
        }
        return ResponseData.error(message, HttpStatus.NOT_ACCEPTABLE);
    }
}
