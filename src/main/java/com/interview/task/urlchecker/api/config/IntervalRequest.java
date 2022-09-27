package com.interview.task.urlchecker.api.config;

import javax.validation.constraints.Positive;

/**
 * Запрос задающий интервал опроса проверяемых URL.
 *
 * @param interval интервал в секундах между проверками доступности URL
 */
public record IntervalRequest(@Positive int interval) {}
