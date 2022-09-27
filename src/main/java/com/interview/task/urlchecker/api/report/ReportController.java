package com.interview.task.urlchecker.api.report;

import com.interview.task.urlchecker.report.AvailabilityReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API получения отчетов.
 */
@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

  private final AvailabilityReportService reportService;

  public ReportController(AvailabilityReportService reportService) {
    this.reportService = reportService;
  }

  /**
   * Отчет о доступности URL.
   *
   * @return список результатов выполнения проверок доступности URL
   */
  @GetMapping("/availability")
  public AvailabilityReportResponse getUrlsAvailability() {
    return new AvailabilityReportResponse(reportService.getReport());
  }
}
