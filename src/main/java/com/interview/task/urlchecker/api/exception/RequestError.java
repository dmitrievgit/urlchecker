package com.interview.task.urlchecker.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Ошибка в REST запросе.
 *
 * @param field имя поля с ошибкой
 * @param error сообщение об ошибке
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RequestError(String field, String error) {}
