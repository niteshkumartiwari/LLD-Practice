package org.example.model;

import org.example.service.SchedulerService;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Job implements Runnable, Comparable<Job> {
    private final int jobId;
    private final Runnable task;

    private final Date startTime;
    private final long reschedulePeriod;

    private final TimeUnit unit;

    private final JobType type;


    public Job(int jobId, Runnable task, Date startTime) {
        this(jobId, task, startTime, -1, TimeUnit.SECONDS, JobType.ONCE);
    }

    public Job(int jobId, Runnable task, Date startTime, long reschedulePeriod, TimeUnit unit, JobType type) {
        this.jobId = jobId;
        this.task = task;
        this.startTime = startTime;
        this.reschedulePeriod = reschedulePeriod;
        this.unit = unit;
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    @Override
    public void run() {
        // AtFixedRate - trigger before - no need to wait for job to complete
        if (JobType.REPEAT_FIXED_RATE.equals(type)) {
            // Recursively schedule the next job before completing the current one
            SchedulerService.getInstance().scheduleAtFixedRate(this.task, reschedulePeriod, reschedulePeriod, unit);
        }

        task.run(); // async

        //WithFixedDelay- trigger after - wait for task to complete
        if (JobType.REPEAT_FIXED_DELAY.equals(type)) {
            // Recursively schedule the next job after completing the current one
            SchedulerService.getInstance().scheduleWithFixedDelay(this.task, reschedulePeriod, reschedulePeriod, unit);
        }
    }

    @Override
    public int compareTo(Job otherJob) {
        return this.startTime.compareTo(otherJob.getStartTime());
    }
}
