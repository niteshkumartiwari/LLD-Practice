package org.example.service;

import org.example.helper.JobExecutor;
import org.example.model.Job;
import org.example.model.JobType;

import java.util.Calendar;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SchedulerService implements ISchedulerService {
    private static final SchedulerService INSTANCE = new SchedulerService(Runtime.getRuntime().availableProcessors() - 1);
    private final PriorityQueue<Job> jobPriorityQueue;
    private final Lock queueLock;
    private final Condition entryAdded;

    private SchedulerService(int threadCount) {
        jobPriorityQueue = new PriorityQueue<>();
        queueLock = new ReentrantLock();
        entryAdded = queueLock.newCondition();

        // Start a separate thread for job executor
        Thread executor = new Thread(new JobExecutor(jobPriorityQueue, queueLock, entryAdded, threadCount));
        executor.start();
    }

    public static SchedulerService getInstance() {
        return INSTANCE;
    }

    private void addToJobQueue(Job job) {
        queueLock.lock();
        try {
//            System.out.println("Adding new job to the queue @t:" + Calendar.getInstance().getTime());
            jobPriorityQueue.offer(job);
            entryAdded.signal();
        } finally {
            queueLock.unlock();
        }
    }

    @Override
    public void schedule(Runnable task, long initialDelay, TimeUnit unit) {
        Date startTime = new Date(Calendar.getInstance().getTime().getTime() + unit.toMillis(initialDelay));
        Job job = new Job(UUID.randomUUID().hashCode(), task, startTime);

        addToJobQueue(job);
    }

    @Override
    public void scheduleAtFixedRate(Runnable task, long initialDelay, long recurringDelay, TimeUnit unit) {
        Date startTime = new Date(Calendar.getInstance().getTime().getTime() + unit.toMillis(initialDelay));
        Job job = new Job(UUID.randomUUID().hashCode(), task, startTime, recurringDelay, unit, JobType.REPEAT_FIXED_RATE);

        addToJobQueue(job);
    }

    @Override
    public void scheduleWithFixedDelay(Runnable task, long initialDelay, long recurringDelay, TimeUnit unit) {
        Date startTime = new Date(Calendar.getInstance().getTime().getTime() + unit.toMillis(initialDelay));
        Job job = new Job(UUID.randomUUID().hashCode(), task, startTime, recurringDelay, unit, JobType.REPEAT_FIXED_DELAY);

        addToJobQueue(job);
    }
}
