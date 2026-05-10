package org.example.aipoweredstudyresourcegenerator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.aipoweredstudyresourcegenerator.Model.DppRequested;
import org.example.aipoweredstudyresourcegenerator.Model.Status;
import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.service.DppService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@RestController
@RequestMapping("/dpp")
@RequiredArgsConstructor
@Tag(name = "DPP", description = "Daily Practice Problems - generate and schedule MCQs")
public class DppController {
    String topic = "";

    private final DppService service;
    private ScheduledFuture<?> schedule;
    private final ThreadPoolTaskScheduler taskScheduler;

    @GetMapping("")
    @Operation(summary = "Generate DPP questions", description = "Immediately generates MCQ questions for the given topic")
    public ResponseEntity<List<Questions>> create(@RequestBody String topic) {
        return new ResponseEntity<>(service.dppGenerator(topic), HttpStatus.OK);
    }

    @PostMapping("schedule")
    @Operation(summary = "Schedule daily DPP", description = "Schedules daily MCQ generation and email delivery at the given time")
    public ResponseEntity<Status> createDpp(@RequestBody DppRequested requested) {
        topic = requested.getTopic();
        Instant firstRun = getFirstRunTime(requested.getTime());

        schedule = taskScheduler.scheduleAtFixedRate(() -> {
            List<Questions> questions = service.dppGenerator(topic);
            service.sendMail(topic, questions);
        }, firstRun, Duration.ofDays(1));

        return new ResponseEntity<>(new Status("scheduled daily", topic), HttpStatus.OK);
    }

    @DeleteMapping("schedule")
    @Operation(summary = "Stop scheduled DPP", description = "Cancels the active daily DPP schedule")
    public ResponseEntity<Status> stopDpp() {
        boolean stopped = schedule.cancel(false);
        topic = "";
        if (stopped) {
            return new ResponseEntity<>(new Status("success", "Scheduled DPP stopped successfully"), HttpStatus.OK);
        }
        return null;
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
}
