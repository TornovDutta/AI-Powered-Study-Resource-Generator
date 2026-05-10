package org.example.aipoweredstudyresourcegenerator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Status", description = "Application health check")
public class StatusController {

    @GetMapping("")
    @Operation(summary = "Health check", description = "Returns a simple status message confirming the app is running")
    public String status() {
        return "The application is running";
    }
}
