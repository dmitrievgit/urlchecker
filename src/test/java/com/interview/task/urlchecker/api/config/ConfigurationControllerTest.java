package com.interview.task.urlchecker.api.config;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.interview.task.urlchecker.configuration.ConfigurationService;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(ConfigurationController.class)
class ConfigurationControllerTest {

  @Autowired private WebTestClient webClient;

  @MockBean ConfigurationService configurationService;

  @ParameterizedTest
  @MethodSource("getUrlsRequestParams")
  void shouldConfigureUrlWhenRequestValid(
      String urlsBody, List<String> urls, HttpStatus status, String responseBody) {
    webClient
        .post()
        .uri("/api/v1/config/urls")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(urlsBody)
        .exchange()
        .expectStatus()
        .isEqualTo(status)
        .expectBody()
        .json(responseBody);

    if (urls != null) {
      verify(configurationService).setCheckingUrls(urls);
    }
  }

  public static Stream<Arguments> getUrlsRequestParams() {
    return Stream.of(
        Arguments.of(
            "{\"urls\":[\"http://ya.ru\", \"http://mail.ru\"]}",
            List.of("http://ya.ru", "http://mail.ru"),
            HttpStatus.OK,
            ""),
        Arguments.of(
            "{\"urls\":[\"http://ya.ru\", \"qqqq\", \"http://mail.ru\"]}",
            null,
            HttpStatus.BAD_REQUEST,
            "[{\"field\":\"urls[1]\",\"error\":\"должно содержать допустимый URL\"}]"),
        Arguments.of(
            "{}",
            null,
            HttpStatus.BAD_REQUEST,
            "[{\"field\":\"urls\",\"error\":\"не должно равняться null\"}]"));
  }

  @ParameterizedTest
  @CsvSource({"-1,false", "1,true", "0,false", "10000,true"})
  void shouldConfigureIntervalWhenRequestValid(int param, boolean valid) {
    webClient
        .post()
        .uri("/api/v1/config/interval")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{\"interval\": " + param + "}")
        .exchange()
        .expectStatus()
        .isEqualTo(valid ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
        .expectBody()
        .json(valid ? "" : "[{\"field\":\"interval\",\"error\":\"должно быть больше 0\"}]");

    if (valid) {
      verify(configurationService).setCheckInterval(param);
    }
  }

  @ParameterizedTest
  @CsvSource({"/api/v1/config/urls", "/api/v1/config/interval"})
  void shouldReturnBadRequestWhenRequestBodyNotValid(String requestUrl) {
    webClient
        .post()
        .uri(requestUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("-")
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .json("{\"error\":\"Failed to read HTTP message\"}");
  }

  @ParameterizedTest
  @CsvSource({"/api/v1/config/urls", "/api/v1/config/interval"})
  void shouldReturnBadRequestWhenEmptyBody(String requestUrl) {
    webClient
        .post()
        .uri(requestUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @ParameterizedTest
  @CsvSource({"/api/v1/config/urls", "/api/v1/config/interval"})
  void shouldReturnUnsupportedMediaTypeWhenContentTypeNotSet(String requestUrl) {
    webClient
        .post()
        .uri(requestUrl)
        .bodyValue("{}")
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @ParameterizedTest
  @CsvSource({"/api/v1/config/urls,", "/api/v1/config/interval"})
  void shouldReturnInternalServerErrorOnException(String requestUrl) {
    doThrow(new IllegalArgumentException()).when(configurationService).setCheckInterval(anyInt());
    doThrow(new IllegalArgumentException()).when(configurationService).setCheckingUrls(anyList());
    webClient
        .post()
        .uri(requestUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{\"interval\": 1, \"urls\": []}")
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
