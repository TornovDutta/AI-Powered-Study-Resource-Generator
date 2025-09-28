package org.example.aipoweredstudyresourcegenerator.controller;
import org.example.aipoweredstudyresourcegenerator.Model.DppRequested;
import org.example.aipoweredstudyresourcegenerator.Model.Status;
import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.config.DynamicSchedule;


import org.example.aipoweredstudyresourcegenerator.service.DppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.*;


import java.time.*;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@RestController
@RequestMapping("/dpp")
public class DppController {
    String topic="";
    private ScheduledFuture<?> scheduled;


    @Autowired
    private DppService service;
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;



    @GetMapping("")
    public ResponseEntity<List<Questions>> create(@RequestBody String topic){
        return new ResponseEntity<>(service.dppGenerator(topic), HttpStatus.OK);
    }
    @PostMapping("schedule")
    public ResponseEntity<Status> createDpp(@RequestBody DppRequested requested){
        LocalTime requestedTime = requested.getTime();
        topic = requested.getTopic();
        if (scheduled != null) {
            scheduled.cancel(false);
        }
        Instant firstRun = getFirstRunTime(requestedTime);

        scheduled = taskScheduler.scheduleAtFixedRate(() -> {
            List<Questions> questions = service.dppGenerator(topic);
            service.sendMail(topic, questions);
        }, firstRun, Duration.ofDays(1));

        Status status = new Status("scheduled daily", topic);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
    private Instant getFirstRunTime(LocalTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withHour(time.getHour())
                .withMinute(time.getMinute())
                .withSecond(0)
                .withNano(0);

        if (nextRun.isBefore(now)) {
            nextRun = nextRun.plusDays(1);
        }

        return nextRun.atZone(ZoneId.systemDefault()).toInstant();
    }



    @DeleteMapping("schedule")
    public ResponseEntity<Status> stopDpp(){
        boolean stopped = scheduled.cancel(false);
        topic = "";
        if(stopped) {
            Status status=new Status("success","Scheduled DPP stopped successfully");
            return new ResponseEntity<>(status, HttpStatus.OK);
        } else {
            return null;
        }
    }
}
