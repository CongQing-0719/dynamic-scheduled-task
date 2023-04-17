package zone.cong.dynamicscheduledtask.entity;

public class TaskInfo {
    private final String id;
    private final String cron;
    private final String content;
    private final boolean isRunning;
    private final String lastExecTime;

    public TaskInfo(String id, String cron, String content, boolean isRunning, String lastExecTime) {
        this.id = id;
        this.cron = cron;
        this.content = content;
        this.isRunning = isRunning;
        this.lastExecTime = lastExecTime;
    }

    public String getId() {
        return id;
    }

    public String getCron() {
        return cron;
    }

    public String getContent() {
        return content;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public String getLastExecTime() {
        return lastExecTime;
    }
}