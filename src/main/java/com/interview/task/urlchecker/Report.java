package com.interview.task.urlchecker;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Report {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String url;

  private boolean available;

  @Column(columnDefinition = "TIMESTAMP")
  private LocalDateTime checkTime;

  protected Report() {}

  public Report(String url, boolean available) {
    this.url = url;
    this.available = available;
    checkTime = LocalDateTime.now();
  }
}
