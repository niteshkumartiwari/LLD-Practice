import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

enum IncidentStatus {
    OPEN,
    WORK_IN_PROGRESS,
    PENDING,
    CLOSED,
}

enum SEVERITY {
    SEV_1,
    SEV_2,
    SEV_2_5,
    SEV_3,
    SEV_4,
    SEV_5
}

class User {
    public String id;
    public User manager;

    public User(String id, User manager) {
        this.id = id;
        this.manager = manager;
    }
}

class Page {
    public Incident incident;
    public User user;
    public LocalDateTime pagedAt;

    public Page(Incident incident, User user) {
        this.incident = incident;
        this.user = user;
        this.pagedAt = LocalDateTime.now();
    }
}

class Incident {
    public String id;
    public String title;
    public String subject;

    public IncidentStatus status;
    public List<User> checkedInUsers;
    public List<Page> pagesHistory;

    public Incident(String id, String title, String subject) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.status = IncidentStatus.OPEN;
        checkedInUsers = new ArrayList<>();
        pagesHistory = new ArrayList<>();
    }
}

interface PageChannel {
    void notify(User user, Incident incident);
}

class SimplePageChannel implements PageChannel{
    @Override
    public void notify(User user, Incident incident) {
        System.out.println("Paging user: "+ user.id+ " for incident: "+ incident.id);
    }
}

interface TaskScheduler {
    void schedule(Runnable runnable, Duration delay);
}

class InMemoryTaskScheduler implements TaskScheduler {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

    @Override
    public void schedule(Runnable runnable, Duration delay) {
        executorService.schedule(runnable, delay.toMillis(), TimeUnit.MILLISECONDS);
    }
}

class OnCallRoster { // Singleton
    public TaskScheduler scheduler = new InMemoryTaskScheduler();
    public PageChannel pager = new SimplePageChannel();
    public Duration ESCALATION_DURATION = Duration.ofSeconds(10);

    public OnCallRoster() {

    }

    public void triggerIncident(Incident incident, User user) {
        pager.notify(user, incident);
        incident.pagesHistory.add(new Page(incident, user));
        scheduler.schedule(() -> escalate(incident), ESCALATION_DURATION);
    }

    private void escalate(Incident incident) {
        if(!incident.status.equals(IncidentStatus.OPEN)) {
            return;
        }

        System.out.println("["+ LocalDateTime.now()+"] Incident-" + incident.id+" is still Open, Escalating");

        User lastPagedEmployee = incident.pagesHistory.get(incident.pagesHistory.size()-1).user;
        User escalationManager = lastPagedEmployee.manager;

        if(escalationManager == null) {
            System.out.println("Escalation chain ended.");
            return;
        }

        pager.notify(escalationManager, incident);
        incident.pagesHistory.add(new Page(incident, escalationManager));

        scheduler.schedule(() -> escalate(incident), ESCALATION_DURATION);
    }
}



class OnCallRosterService {
    public static void main(String[] args) {
        User andy = new User("0", null);
        User paras = new User("1", andy);
        User naveen = new User("2", paras);
        User rajat = new User("3", naveen);
        User nitesh = new User("4", rajat);

        Incident Egress_5XX_SPIKE = new Incident("1", "Egress 5XX Spike", "Queries are failing");
        OnCallRoster onCallRoster = new OnCallRoster();
        onCallRoster.triggerIncident(Egress_5XX_SPIKE, nitesh);
    }
}
