package org.example.aipoweredstudyresourcegenerator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.aipoweredstudyresourcegenerator.Model.Status;
import org.example.aipoweredstudyresourcegenerator.Model.TestRequested;
import org.example.aipoweredstudyresourcegenerator.Model.Questions;
import org.example.aipoweredstudyresourcegenerator.config.DynamicSchedule;
import org.example.aipoweredstudyresourcegenerator.service.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tests/")
@Tag(name = "Tests", description = "Generate and schedule AI-powered tests")
public class TestController {
    String topic;

    private final TestService service;
    private final DynamicSchedule dynamicScheduler;

    public TestController(TestService service, DynamicSchedule dynamicScheduler) {
        this.service = service;
        this.dynamicScheduler = dynamicScheduler;
    }

    @PostMapping("")
    @Operation(summary = "Generate a test", description = "Immediately generates MCQ test questions for the given topic")
    public ResponseEntity<List<Questions>> create(@RequestBody String topic) {
        return new ResponseEntity<>(service.testGenerator(topic), HttpStatus.OK);
    }

    @PostMapping("/schedule")
    @Operation(summary = "Schedule a test", description = "Schedules a one-time test and sends results via email")
    public ResponseEntity<Status> createTest(@RequestBody TestRequested testRequested) {
        topic = testRequested.getTopic();
        LocalDateTime dateTime = LocalDateTime.of(testRequested.getDate(), testRequested.getTime());

        dynamicScheduler.schedule(() -> {
            List<Questions> questions = service.testGenerator(topic);
            service.sendMail(topic, questions);
        }, dateTime);

        return new ResponseEntity<>(new Status("scheduled", testRequested.getTopic()), HttpStatus.OK);
    }

    @DeleteMapping("/schedule")
    @Operation(summary = "Stop scheduled test", description = "Cancels the active scheduled test")
    public ResponseEntity<Status> stopTest() {
        boolean stopped = dynamicScheduler.stop();
        topic = "";
        if (stopped) {
            return new ResponseEntity<>(new Status("success", "Scheduled test stopped successfully"), HttpStatus.OK);
        }
        return null;
    }
}
