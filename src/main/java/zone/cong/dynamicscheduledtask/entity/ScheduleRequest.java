package zone.cong.dynamicscheduledtask.entity;

public class ScheduleRequest {
    private String cron;
    private String content;

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
}