package com.interview.task.urlchecker.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interview.task.urlchecker.Task;
import com.interview.task.urlchecker.TaskRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConfigurationServiceTest {

  @Mock TaskRepository repository;
  ConfigurationService service;

  @BeforeEach
  public void setUp() {
    service = new ConfigurationService(repository, 1000);
  }

  @Test
  void shouldChangeCheckingUrls() {
    var urls = List.of("http://ya.ru", "http://mail.ru");

    service.setCheckingUrls(urls);

    var capturedTasks = ArgumentCaptor.forClass(Task.class);
    verify(repository).deleteAll();
    verify(repository, times(2)).save(capturedTasks.capture());
    assertEquals(2, capturedTasks.getAllValues().size());
    assertEquals(urls.get(0), capturedTasks.getAllValues().get(0).getUrl());
    assertEquals(urls.get(1), capturedTasks.getAllValues().get(1).getUrl());
  }

  @Test
  void shouldGetCheckingUrls() {
    var tasks = List.of(new Task("http://ya.ru"), new Task("http://mail.ru"));
    var expectedUrls = List.of("http://ya.ru", "http://mail.ru");

    when(repository.findAll()).thenReturn(tasks);

    var urls = service.getUrls();

    verify(repository).findAll();
    assertEquals(expectedUrls, urls);
  }

  @Test
  void shouldChangeInterval() {
    service.setCheckInterval(2000);
    assertEquals(2000, service.getCheckInterval());
  }
}
