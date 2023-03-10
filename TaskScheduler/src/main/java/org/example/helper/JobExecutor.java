package org.example.helper;

import org.example.model.Job;

import java.util.Calendar;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class JobExecutor implements Runnable {
    private final Executor jobExecutor;
    private final PriorityQueue<Job> jobPriorityQueue;
    private final Lock queueLock;
    private final Condition entryAdded;

    public JobExecutor(PriorityQueue<Job> jobPriorityQueue, Lock queueLock, Condition entryAdded, int threadCount) {
        this.jobPriorityQueue = jobPriorityQueue;
        this.queueLock = queueLock;
        this.entryAdded = entryAdded;
        this.jobExecutor = Executors.newFixedThreadPool(threadCount);
    }

    @Override
    public void run() {
//        System.out.println("Running job executor");
        while (true) {
//            System.out.println("Running job executor(Inside While)");
            queueLock.lock();
            try {
                Date currentTime = Calendar.getInstance().getTime();
                if (!jobPriorityQueue.isEmpty()) {
                    Job job = jobPriorityQueue.peek();
                    Date startTime = job.getStartTime();

                    if (currentTime.compareTo(startTime) >= 0) {
                        jobPriorityQueue.remove();

                        jobExecutor.execute(job);
                    }
                }

                if (jobPriorityQueue.isEmpty()) { // if its empty after execution
                    entryAdded.await();
                } else { // got to sleep till next earliest job
                    Job job = jobPriorityQueue.peek();
                    Date startTime = job.getStartTime();
                    long sleepTime = startTime.getTime() - currentTime.getTime();
//                    System.out.println("Going to sleep till next earliest job for t:" + sleepTime);
                    entryAdded.await(sleepTime, TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                queueLock.unlock();
            }
        }
    }
}
