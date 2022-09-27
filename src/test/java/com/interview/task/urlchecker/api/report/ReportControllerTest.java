package com.interview.task.urlchecker.api.report;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interview.task.urlchecker.report.AvailabilityReportService;
import com.interview.task.urlchecker.report.CheckResult;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(ReportController.class)
class ReportControllerTest {

  @Autowired private WebTestClient webClient;

  @MockBean AvailabilityReportService reportService;

  @Test
  void shouldReturnReport() {
    LocalDateTime expectedDatTime = LocalDateTime.of(1986, 4, 14, 20, 11, 0);
    when(reportService.getReport())
        .thenReturn(List.of(new CheckResult(expectedDatTime, "url", true)));

    webClient
        .get()
        .uri("/api/v1/report/availability")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json(
            """
            {"checkResults":[{"time":"1986-04-14 20:11:00.000","url":"url","available":true}]}
            """);

    verify(reportService).getReport();
  }

  @Test
  void shouldReturnInternalServerErrorOnException() {
    doThrow(new IllegalArgumentException()).when(reportService).getReport();
    webClient
        .get()
        .uri("/api/v1/report/availability")
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
