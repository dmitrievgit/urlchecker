package com.interview.task.urlchecker.api.report;

import com.interview.task.urlchecker.report.CheckResult;
import java.util.List;

/**
 * Запрос задающий список проверяемых URL.
 *
 * @param checkResults список результатов проверки URL
 */
public record AvailabilityReportResponse(List<CheckResult> checkResults) {}
