package org.example.aipoweredstudyresourcegenerator.Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class DppRequested {
    String topic;
    LocalDate date;
    LocalTime time;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
