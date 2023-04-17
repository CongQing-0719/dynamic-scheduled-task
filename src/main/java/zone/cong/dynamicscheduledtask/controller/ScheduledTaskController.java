package zone.cong.dynamicscheduledtask.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zone.cong.dynamicscheduledtask.entity.ScheduleRequest;
import zone.cong.dynamicscheduledtask.entity.ScheduledTask;
import zone.cong.dynamicscheduledtask.entity.TaskInfo;

@RestController
public class ScheduledTaskController {
    @Autowired
    private TaskScheduler scheduler;

    // 存储任务信息，key 为任务 ID，value 为任务信息
    private Map<String, ScheduledTask> tasks = new ConcurrentHashMap<>();

    @GetMapping("/tasks")
    public List<TaskInfo> listTasks() {
        List<TaskInfo> result = new ArrayList<>();
        for (ScheduledTask task : tasks.values()) {
            boolean isRunning = task.getFuture() != null && !task.getFuture().isCancelled();
            LocalDateTime lastExecTime = task.getLastExecTime();
            String lastExecTimeStr = lastExecTime != null ? lastExecTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "";
            result.add(new TaskInfo(task.getId(), task.getCron(), task.getContent(), isRunning, lastExecTimeStr));
        }
        return result;
    }

    @PostMapping("/tasks")
    public void scheduleTask(@RequestBody ScheduleRequest request) {
        ScheduledTask task = new ScheduledTask(request.getCron(), request.getContent());
        ScheduledFuture<?> future = scheduler.schedule(task, new CronTrigger(request.getCron()));
        task.setFuture(future);
        tasks.put(task.getId(), task);
    }

    @DeleteMapping("/tasks/{id}")
    public void cancelTask(@PathVariable String id) {
        ScheduledTask task = tasks.remove(id);
        if (task != null) {
            task.getFuture().cancel(true);
        }
    }

    @PostMapping("/tasks/{id}")
    public void updateTask(@PathVariable String id, @RequestBody ScheduleRequest request) {
        ScheduledTask task = tasks.remove(id);
        if (task != null) {
            task.setCron(request.getCron());
            task.setContent(request.getContent());
            task.getFuture().cancel(true);
            ScheduledFuture<?> future = scheduler.schedule(task, new CronTrigger(request.getCron()));
            task.setFuture(future);
            tasks.put(task.getId(), task);
        }
    }

    @PostMapping("/tasks/{id}/start")
    public void startTask(@PathVariable String id) {
        ScheduledTask task = tasks.get(id);
        if (task != null && task.getFuture() == null) {
            LocalDateTime lastExecTime = LocalDateTime.now();
            ScheduledFuture<?> future = scheduler.schedule(task, new CronTrigger(task.getCron()));
            task.setFuture(future);
            task.setLastExecTime(lastExecTime);
        }
    }

    @PostMapping("/tasks/{id}/stop")
    public void stopTask(@PathVariable String id) {
        ScheduledTask task = tasks.get(id);
        if (task != null && task.getFuture() != null) {
            task.getFuture().cancel(true);
            task.setFuture(null);
        }
    }

    @PostMapping("/tasks/startAll")
    public void startAllTasks() {
        for (ScheduledTask task : tasks.values()) {
            if (task.getFuture() == null) {
                LocalDateTime lastExecTime = LocalDateTime.now();
                ScheduledFuture<?> future = scheduler.schedule(task, new CronTrigger(task.getCron()));
                task.setFuture(future);
                task.setLastExecTime(lastExecTime);
            }
        }
    }

    @PostMapping("/tasks/stopAll")
    public void stopAllTasks() {
        for (ScheduledTask task : tasks.values()) {
            if (task.getFuture() != null) {
                task.getFuture().cancel(true);
                task.setFuture(null);
            }
        }
    }
}