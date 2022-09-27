package com.interview.task.urlchecker.api.exception;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

/**
 * Обработчики исключений REST запросов.
 */
@Slf4j
@ControllerAdvice(annotations = RestController.class)
@RestController
public class RestExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ServerWebInputException.class)
  public RequestError handleServerWebInputException(ServerWebInputException ex) {
    return new RequestError(null, ex.getReason());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(WebExchangeBindException.class)
  public List<RequestError> handleWebExchangeBindException(WebExchangeBindException ex) {
    return getResponseEntity(ex.getBindingResult());
  }

  private List<RequestError> getResponseEntity(BindingResult bindingResult) {
    return bindingResult.getFieldErrors().stream()
        .map(fieldError -> new RequestError(fieldError.getField(), fieldError.getDefaultMessage()))
        .toList();
  }
}
