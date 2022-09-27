package com.interview.task.urlchecker.scheduler;

import com.interview.task.urlchecker.Report;
import com.interview.task.urlchecker.ReportRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Выполняет проверку доступности URL и сохраняет в БД ее результат.
 */
@Slf4j
@Service
public class AvailabilityChecker {

  private final WebClient webClient;
  private final ReportRepository repository;

  public AvailabilityChecker(WebClient webClient, ReportRepository repository) {
    this.webClient = webClient;
    this.repository = repository;
  }

  /**
   * Проверяет доступность списка URL, результаты проверки сохраняются в БД.
   *
   * @param urls список URL для проверки
   */
  public void check(List<String> urls) {
    log.info("URL availability check executed: urls={}", urls);
    urls.forEach(this::check);
  }

  private void check(String url) {
    webClient
        .get()
        .uri(url)
        .retrieve()
        .toBodilessEntity()
        .doOnSuccess(
            r -> {
              log.info("Available URL: {}", url);
              repository.save(new Report(url, true));
            })
        .doOnError(
            r -> {
              log.info("Unavailable URL: {}", url);
              repository.save(new Report(url, false));
            })
        .onErrorResume(throwable -> Mono.empty())
        .subscribe();
  }
}
