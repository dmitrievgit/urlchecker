package com.interview.task.urlchecker.scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interview.task.urlchecker.Report;
import com.interview.task.urlchecker.ReportRepository;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class AvailabilityCheckerTest {
  AvailabilityChecker checker;
  @Mock ReportRepository repository;
  @Mock private ExchangeFunction exchangeFunction;

  @BeforeEach
  public void setUp() {
    WebClient webClient = WebClient.builder().exchangeFunction(exchangeFunction).build();
    checker = new AvailabilityChecker(webClient, repository);
  }

  @ParameterizedTest
  @CsvSource({"url1,400,false", "url2,200,true", "url3,500,false"})
  void shouldSaveCheckResult(String url, int responseCode, boolean isAvailable) {
    when(exchangeFunction.exchange(any(ClientRequest.class)))
        .thenReturn(
            Mono.just(
                ClientResponse.create(responseCode, ExchangeStrategies.withDefaults()).build()));

    checker.check(Collections.singletonList(url));

    var capturedReport = ArgumentCaptor.forClass(Report.class);
    verify(repository).save(capturedReport.capture());
    assertEquals(url, capturedReport.getValue().getUrl());
    assertEquals(isAvailable, capturedReport.getValue().isAvailable());
  }
}
