import java.util.Calendar;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

enum JobType {
    FIXED,
    RECURRING
}

class Job implements Runnable, Comparable<Job> {
    public String jobId;
    public Runnable task;
    public Date startTime;
    public JobType jobType;
    public long reschedulePeriod;
    public TimeUnit timeUnit;

    public Job(String jobId, Runnable task, Date startTime) {
        this(jobId, task, startTime, -1, TimeUnit.SECONDS, JobType.FIXED);
    }

    public Job(String jobId, Runnable task, Date startTime, long reschedulePeriod, TimeUnit timeUnit, JobType jobType) {
        this.jobId = jobId;
        this.task = task;
        this.startTime = startTime;
        this.reschedulePeriod = reschedulePeriod;
        this.timeUnit = timeUnit;
        this.jobType = jobType;
    }
    

    @Override
    public void run(){
        try {
            System.out.println(" ======================== ");
            System.out.println("Starting to run job - "+ jobId+ " at time: "+ Calendar.getInstance().getTime());
            task.run();
            System.out.println("Completed the job - "+ jobId+ " at time: "+ Calendar.getInstance().getTime());
        } catch (Exception e) {
            System.out.println("Exception occured while executing job - " + jobId);
            e.printStackTrace();
        }
        
    }

    @Override
    public int compareTo(Job other) {
        return this.startTime.compareTo(other.startTime);
    }
}

class JobExecutor implements Runnable {
    private Executor jobExecutor;
    private PriorityQueue<Job> jobQueue;
    private Lock queueLock;
    private Condition jobAddedInQueue;

    public JobExecutor(PriorityQueue<Job> jobQueue, Lock queueLock, Condition jobAddedInQueue, int jobExectorThreads) {
        this.jobQueue = jobQueue;
        this.queueLock = queueLock;
        this.jobExecutor = Executors.newFixedThreadPool(jobExectorThreads);
        this.jobAddedInQueue = jobAddedInQueue;
    }

    @Override
    public void run(){
        while (true) {
            queueLock.lock();
            try{
                Date currentTime = Calendar.getInstance().getTime();
                if(!jobQueue.isEmpty()){
                    Job job = jobQueue.peek();
                    if(currentTime.compareTo(job.startTime) >= 0){
                        jobQueue.remove();
                        executeJob(job);
                    }
                }

                if(jobQueue.isEmpty()) { // if its empty after execution
                    jobAddedInQueue.await();
                } else { // Go to sleep until next earliest job
                    Job job = jobQueue.peek();
                    Date startTime = job.startTime;
                    long sleepTime = startTime.getTime() - currentTime.getTime(); 
                    jobAddedInQueue.await(sleepTime, TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                queueLock.unlock();
            }
        }
    }

    private void executeJob(Job job){ // Handle recurring and fixed job
        jobExecutor.execute(job);

        if(JobType.RECURRING.equals(job.jobType)) {
            job.startTime = new Date(job.startTime.getTime() + job.timeUnit.toMillis(job.reschedulePeriod));
            SchedulerService.INSTANCE.addToJobQueue(job);;
        }
    }
}

class SchedulerService {
    public static final SchedulerService INSTANCE = new SchedulerService(Runtime.getRuntime().availableProcessors()-1);

    private PriorityQueue<Job> jobQueue;
    private Lock queueLock;
    private AtomicInteger jobCounter; 
    private Condition jobAddedInQueue;

    public SchedulerService(int threadCount) {
        this.jobQueue = new PriorityQueue<>();
        this.queueLock = new ReentrantLock();
        this.jobCounter = new AtomicInteger(0);
        this.jobAddedInQueue = queueLock.newCondition();
        Thread jobExecutor = new Thread(new JobExecutor(jobQueue, queueLock, jobAddedInQueue, threadCount));
        jobExecutor.start();
    }

    public void scheduleOnceJob(Runnable runnable, long initialDelay, TimeUnit unit) {
        Date startTime = new Date(Calendar.getInstance().getTime().getTime() + unit.toMillis(initialDelay));
        Job job = new Job(String.valueOf(jobCounter.addAndGet(1)), runnable, startTime);
        addToJobQueue(job);
    }

    public void scheduleRecurringJob(Runnable runnable, long initialDelay, long recurringDelay, TimeUnit unit) {
        Date startTime = new Date(Calendar.getInstance().getTime().getTime() + unit.toMillis(initialDelay));
        Job job = new Job(String.valueOf(jobCounter.addAndGet(1)), runnable, startTime, recurringDelay, unit, JobType.RECURRING);
        addToJobQueue(job);
    }

    public void addToJobQueue(Job job) {
        queueLock.lock();
        try{
            jobQueue.add(job);
            jobAddedInQueue.signal();
            System.out.println("Successfully added jobId: "+ job.jobId + " in the jobQueue");
        } finally {
            queueLock.unlock();
        }
    }
}

class TaskSchedulerLatest {
    public static void main(String[] args) {
        Runnable task1 = () -> System.out.println("Running task1 (one time)");
        Runnable task2 = () -> System.out.println("Running task2 (recurring time)");
        Runnable task3= () -> {
            System.out.println("Running task3 with exception");
            throw new RuntimeException("Task3 exception");
        };
        
        // SchedulerService.INSTANCE.scheduleOnceJob(task1 , 0, TimeUnit.MILLISECONDS);
        SchedulerService.INSTANCE.scheduleOnceJob(task3 , 0, TimeUnit.MILLISECONDS);
        // SchedulerService.INSTANCE.scheduleRecurringJob(task2, 0, 5, TimeUnit.SECONDS);
    }
}