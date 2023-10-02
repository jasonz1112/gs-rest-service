package com.example.restservice.model;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Session {
    public long duration;
    public List<String> pages;

    public long startTime;

    public Session(long duration, List<String> pages, long startTime) {
        this.duration = duration;
        this.pages = pages;
        this.startTime = startTime;
    }
}
