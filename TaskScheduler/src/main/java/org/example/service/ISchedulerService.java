package org.example.service;

import java.util.concurrent.TimeUnit;

public interface ISchedulerService {
    void schedule(Runnable task, long initialDelay, TimeUnit unit);

    void scheduleAtFixedRate(Runnable task, long initialDelay, long recurringDelay, TimeUnit unit);

    void scheduleWithFixedDelay(Runnable task, long initialDelay, long recurringDelay, TimeUnit unit);
}
