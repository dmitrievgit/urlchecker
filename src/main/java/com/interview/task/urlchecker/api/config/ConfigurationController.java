package com.interview.task.urlchecker.api.config;

import com.interview.task.urlchecker.configuration.ConfigurationService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API настройки опроса URL.
 */
@Validated
@RestController
@RequestMapping("/api/v1/config")
public class ConfigurationController {

  private final ConfigurationService configurationService;

  public ConfigurationController(ConfigurationService configurationService) {
    this.configurationService = configurationService;
  }

  @PostMapping("/urls")
  public ResponseEntity<Void> setUrls(@RequestBody @Valid UrlsRequest urlsRequest) {

    configurationService.setCheckingUrls(urlsRequest.urls());

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/interval")
  public ResponseEntity<Void> setInterval(@RequestBody @Valid IntervalRequest intervalRequest) {

    configurationService.setCheckInterval(intervalRequest.interval());

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
