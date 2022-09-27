package com.interview.task.urlchecker.report;

import com.interview.task.urlchecker.ReportRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Получение отчета о результатах выполненных проверок доступности списка URL.
 */
@Service
public class AvailabilityReportService {

  private final ReportRepository repository;

  public AvailabilityReportService(ReportRepository repository) {
    this.repository = repository;
  }

  /**
   * Формирует отчет доступности списка URL.
   *
   * @return отчет о результатах выполненных проверок доступности списка URL
   */
  public List<CheckResult> getReport() {
    return repository.findAll().stream().map(
            report -> new CheckResult(report.getCheckTime(), report.getUrl(), report.isAvailable()))
        .toList();
  }
}
