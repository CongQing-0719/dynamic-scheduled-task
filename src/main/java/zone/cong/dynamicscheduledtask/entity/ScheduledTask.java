package zone.cong.dynamicscheduledtask.entity;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;

public class ScheduledTask implements Runnable {
    private final String id;
    private String cron;
    private String content;

    private ScheduledFuture<?> future;
    private LocalDateTime lastExecTime;

    public ScheduledTask(String cron, String content) {
        this.id = String.valueOf(System.nanoTime());
        this.cron = cron;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ScheduledFuture<?> getFuture() {
        return future;
    }

    public LocalDateTime getLastExecTime() {
        return lastExecTime;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

    public void setLastExecTime(LocalDateTime lastExecTime) {
        this.lastExecTime = lastExecTime;
    }

    @Override
    public void run() {
        System.out.println(content);
        setLastExecTime(LocalDateTime.now());
    }
}