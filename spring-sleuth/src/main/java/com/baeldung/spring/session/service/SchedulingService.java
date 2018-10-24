package com.baeldung.spring.session.service;

import com.baeldung.spring.session.service.SleuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulingService {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  private final SleuthService sleuthService;

  @Autowired
  public SchedulingService(SleuthService sleuthService) {
    this.sleuthService = sleuthService;
  }

  @Scheduled(fixedDelay = 30000)
  public void scheduledWork() throws InterruptedException {
    logger.info("I want to check MDC:" + MDC.get("X-B3-TraceId"));
    logger.info("Start some work from the scheduled task - asyncMethod");
    sleuthService.asyncMethod();
    logger.info("End work from scheduled task - asyncMethod");
  }
}
