package com.geodesic.adaas.system.controller;

import com.geodesic.adaas.system.dto.Errors;
import com.geodesic.adaas.system.dto.ProblemDetails;
import com.geodesic.adaas.system.exception.RecordNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(RecordNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public void handleRecordNotFound(RecordNotFoundException e) {
    // No response necessary
  }

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ProblemDetails onConstraintViolationException(BindException validationException) {
    log.error("Constraint violation " + validationException);
    List<FieldError> fieldErrors = new ArrayList<>(validationException.getFieldErrors());
    fieldErrors.sort(Comparator.comparing(FieldError::getField));
    String message =
        fieldErrors.stream()
            .map(error -> "'" + error.getField() + "' " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
    return new ProblemDetails(
        Errors.INVALID_PAYLOAD.getCode(), Errors.INVALID_PAYLOAD.getTitle(), message);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ProblemDetails onDataIntegrityViolationException(
      DataIntegrityViolationException integrityViolationException) {
    log.error("Database integrity violation", integrityViolationException);
    return new ProblemDetails(
        Errors.ALREADY_EXISTS.getCode(), Errors.INVALID_PAYLOAD.getTitle(), "Data violation");
  }

  @ExceptionHandler(
      value = {MethodArgumentTypeMismatchException.class, MissingRequestHeaderException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ProblemDetails onTypeMismatchException(Exception exception) {
    log.error("Type mismatch exception ", exception);
    return new ProblemDetails(
        Errors.INVALID_PAYLOAD.getCode(),
        Errors.INVALID_PARAMETERS.getTitle(),
        exception.getMessage());
  }
}
