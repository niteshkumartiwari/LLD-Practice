package org.example;

import org.example.service.ISchedulerService;
import org.example.service.SchedulerService;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Driver {
    public static void main(String[] args) {
        ISchedulerService service = SchedulerService.getInstance();

        System.out.println("@t:"+ Calendar.getInstance().getTime());

        Runnable task1 = () -> System.out.println("Running task1 (one time) @t:"+ Calendar.getInstance().getTime());
        Runnable task2 = () -> System.out.println("Running task2 (one time) @t:"+ Calendar.getInstance().getTime());

        service.schedule(task1, 10, TimeUnit.SECONDS);
        service.schedule(task2, 5, TimeUnit.SECONDS);

        Runnable task3 = () -> System.out.println("Running task3 (at fixed rate) @t:"+ Calendar.getInstance().getTime());
        service.scheduleAtFixedRate(task3, 3, 2, TimeUnit.SECONDS);

        Runnable task4= () -> {
            System.out.println("Running task4 (with 1s processing delay) @t:"+ Calendar.getInstance().getTime());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        service.scheduleWithFixedDelay(task4, 1, 3, TimeUnit.SECONDS);
    }
}