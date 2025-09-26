package com.bldrei.sectors.exception;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@SuppressWarnings("unused")
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ErrorResponse handleError(BusinessException e) {
    log.debug("Business logic exception", e);

    return new ErrorResponse(e.getMessage());
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleError(BadRequestException e) {
    log.debug("Common bad request exception", e);

    return new ErrorResponse(e.getMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleError(NotFoundException e) {
    log.debug("Object not found", e);

    return new ErrorResponse(e.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleError(RuntimeException e) {
    log.error("Internal server error", e);

    return new ErrorResponse(e.getMessage());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                @NonNull HttpHeaders headers,
                                                                @NonNull HttpStatusCode status,
                                                                @NonNull WebRequest request) {
    log.debug("Validation error occurred.", ex);

    List<Map<String, String>> errors = ex.getBindingResult().getAllErrors()
      .stream()
      .map(error -> Map.of(
        "field", ((FieldError) error).getField(),
        "message", error.getDefaultMessage()
      )).toList();

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }
}
