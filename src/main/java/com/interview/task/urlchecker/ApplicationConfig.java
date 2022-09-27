package com.interview.task.urlchecker;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class ApplicationConfig {

  @Bean
  public WebClient webClient(@Value("${request.timeout}") int timeout) {
    var httpClient =
        HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
            .doOnConnected(
                connection ->
                    connection.addHandlerLast(
                        new ReadTimeoutHandler(timeout, TimeUnit.MILLISECONDS)));
    return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
  }

  @Bean
  public Executor scheduledTaskExecutor() {
    return Executors.newScheduledThreadPool(1);
  }
}
