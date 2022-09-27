package com.interview.task.urlchecker.scheduler;

import com.interview.task.urlchecker.configuration.ConfigurationService;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Настраивает проверку набора URL с заданным в настройках интервалом.
 */
@Configuration
@EnableScheduling
public class AvailabilityCheckTaskConfig implements SchedulingConfigurer {

  private final Executor scheduledTaskExecutor;
  private final AvailabilityChecker checker;
  private final ConfigurationService configurationService;

  public AvailabilityCheckTaskConfig(
      Executor scheduledTaskExecutor,
      AvailabilityChecker checkerClient,
      ConfigurationService configurationService) {
    this.scheduledTaskExecutor = scheduledTaskExecutor;
    this.checker = checkerClient;
    this.configurationService = configurationService;
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler(scheduledTaskExecutor);
    taskRegistrar.addTriggerTask(
        () -> checker.check(configurationService.getUrls()),
        triggerContext -> {
          Calendar nextExecutionTime = new GregorianCalendar();
          Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
          nextExecutionTime.setTime(
              lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
          nextExecutionTime.add(Calendar.MILLISECOND, configurationService.getCheckInterval());
          return nextExecutionTime.getTime();
        });
  }
}
