package com.interview.task.urlchecker.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Результат проверки доступности URL.
 *
 * @param time      время выполнения проверки
 * @param url       проверяемый URL
 * @param available результат проверки
 */
public record CheckResult(@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS") LocalDateTime time,
                          String url,
                          boolean available) {}
