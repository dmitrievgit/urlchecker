package com.interview.task.urlchecker.configuration;

import com.interview.task.urlchecker.Task;
import com.interview.task.urlchecker.TaskRepository;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Сервис настройки параметров проверки URL.
 */
@Slf4j
@Service
public class ConfigurationService {

  private final TaskRepository repository;

  private int interval;

  public ConfigurationService(
      TaskRepository repository, @Value("${scheduler.default-interval}") int interval) {
    this.repository = repository;
    this.interval = interval;
  }

  /**
   * Задает список URL доступность которых будет проверяться.
   *
   * @param urls список URL
   */
  @Transactional
  public void setCheckingUrls(@NonNull List<String> urls) {
    Assert.notNull(urls, "List of URLs cannot be null.");
    repository.deleteAll();
    urls.stream().filter(Objects::nonNull).map(Task::new).forEach(repository::save);
    log.info("List of checking URLs changed. New list={}", urls);
  }

  /**
   * Возвращает список URL для проверки их доступности.
   *
   * @return Список URL для проверки
   */
  public List<String> getUrls() {
    var tasks = repository.findAll();
    return tasks.stream().map(Task::getUrl).toList();
  }

  /**
   * Задает интервал проверок URL.
   *
   * @param interval интервал проверок в мс
   */
  public void setCheckInterval(int interval) {
    log.info("Scheduler interval changed. New interval={}ms", interval);
    this.interval = interval;
  }

  /**
   * Возвращает интервал проверок доступности URL.
   *
   * @return интервал проверок в мс
   */
  public int getCheckInterval() {
    return interval;
  }
}
