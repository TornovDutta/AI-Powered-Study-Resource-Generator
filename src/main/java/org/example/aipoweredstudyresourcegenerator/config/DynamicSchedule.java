package org.example.aipoweredstudyresourcegenerator.config;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import java.util.concurrent.ScheduledFuture;
@Component
public class DynamicSchedule {
    private final TaskScheduler scheduler;
    private ScheduledFuture<?> scheduledTask;

    public DynamicSchedule() {
        ThreadPoolTaskScheduler taskScheduler=new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
        this.scheduler = taskScheduler;
    }
    public void schedule(Runnable task, LocalDateTime dateTime){
        Date triggerDate=Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        scheduledTask=scheduler.schedule(task,triggerDate);
    }
    public boolean stop(){
        if(scheduledTask!=null && !scheduledTask.isDone()){
            return scheduledTask.cancel(false);
        }
        return false;
    }
}
