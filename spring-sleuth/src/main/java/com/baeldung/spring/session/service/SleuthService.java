package com.baeldung.spring.session.service;

import brave.Span;
import brave.Tracer;
import brave.Tracer.SpanInScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SleuthService {
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  private final Tracer tracer;

  @Autowired
  public SleuthService(Tracer tracer) {
    this.tracer = tracer;
  }

  public void doSomeWorkSameSpan() throws InterruptedException {
    //    Thread.sleep(1000L);
    randomBadLuck();
    logger.info("Doing some work");
  }

  public void doSomeWorkNewSpan() throws InterruptedException {
    logger.info("I'm in the original span");

    Span newSpan = tracer.newTrace().name("newSpan").start();
    try (SpanInScope ws = tracer.withSpanInScope(newSpan.start())) {
      //      Thread.sleep(1000L);
      logger.info("I'm in the new span doing some cool work that needs its own span");
    } finally {
      newSpan.finish();
    }
    randomBadLuck();
    logger.info("I'm in the original span");
  }

  @Async
  public void asyncMethod() throws InterruptedException {

    logger.info("Start Async Method");
    //    Thread.sleep(1000L);
    long badLuckTime = System.currentTimeMillis();
    try {
      randomBadLuck();
    } catch (Exception ex) {
      logger.error("You are bad luck at:{}", badLuckTime);
    }
    logger.info("End Async Method");
  }

  public void randomBadLuck() {
    long badLuckTime = System.nanoTime();
    if (badLuckTime % 2 == 0) {
      throw new RuntimeException("You are bad luck:" + badLuckTime);
    }
  }
}
