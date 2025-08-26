package org.example.aipoweredstudyresourcegenerator.Model;


import java.time.LocalDate;
import java.time.LocalTime;

public class DppRequested {
    String topic;
    LocalTime time;

    public void setTopic(String topic) {
        this.topic = topic;
    }





    public LocalTime getTime() {
        return time;
    }
    public String getTopic() {
        return topic;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

}
