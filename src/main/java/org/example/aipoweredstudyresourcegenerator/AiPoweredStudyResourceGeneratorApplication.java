package org.example.aipoweredstudyresourcegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiPoweredStudyResourceGeneratorApplication {

    public static void main(String[] args) {
        System.out.println("Start the project");
        SpringApplication.run(AiPoweredStudyResourceGeneratorApplication.class, args);
    }

}
