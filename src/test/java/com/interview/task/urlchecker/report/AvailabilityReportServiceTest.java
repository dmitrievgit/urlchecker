package com.interview.task.urlchecker.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.interview.task.urlchecker.Report;
import com.interview.task.urlchecker.ReportRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AvailabilityReportServiceTest {
  @InjectMocks AvailabilityReportService service;
  @Mock ReportRepository repository;

  @Test
  void shouldReturnCheckResult() {
    var report = List.of(new Report("url1", true), new Report("url2", false));
    var expectedResult =
        List.of(
            new CheckResult(report.get(0).getCheckTime(), "url1", true),
            new CheckResult(report.get(1).getCheckTime(), "url2", false));
    when(repository.findAll()).thenReturn(report);

    var result = service.getReport();

    assertEquals(expectedResult, result);
  }
}
