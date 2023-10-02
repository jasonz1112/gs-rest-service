package com.example.restservice.controller;

import com.example.restservice.model.Event;
import com.example.restservice.model.Session;
import com.example.restservice.service.SessionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class SessionController {

    @Autowired
    SessionServiceImpl sessionServiceImpl;

    private String postResultUrl = "https://candidate.hubteam.com/candidateTest/v3/problem/result?userKey=297df2ff1740b5aa8a3975b1ae2b";

    @GetMapping("/events")
    public String getEvents() throws IOException {
        List<Event> eventList = sessionServiceImpl.getEvents();

        Map<String, List<Event>> eventByUserMap = new HashMap<>();
        Map<String, List<Session>> sessionMap = new HashMap<>();

        for (Event event : eventList) {
            if (eventByUserMap.containsKey(event.visitorId)) {
                eventByUserMap.get(event.visitorId).add(event);
            } else {
                List<Event> events = new ArrayList<>();
                events.add(event);
                eventByUserMap.put(event.visitorId, events);
            }
        }

        for (Map.Entry<String, List<Event>> eventByUserEntry : eventByUserMap.entrySet()) {
            System.out.println("Key = " + eventByUserEntry.getKey() +
                    ", Value = " + eventByUserEntry.getValue());
            List<Event> events = eventByUserEntry.getValue();

            // sort the event by the particular visitorId based on timestamp
            events.sort((o1, o2) -> (int) (o1.timestamp - o2.timestamp));

            // create a new list of sessions for the particular visitorId
            List<Session> sessions = new ArrayList<>();
            List<String> currentPages = new ArrayList<>();


            int i = 1;
            while (i < events.size()) {
                // last timestamp of the previous session
                long startTime = events.get(i-1).timestamp;
                String currentPage = events.get(i-1).url;
                if (!currentPages.contains(currentPage)) {
                    currentPages.add(currentPage);
                }

                long duration = 0L;

                // accumulate pages belong to the same session
                while (i < events.size() && events.get(i).timestamp - events.get(i-1).timestamp <= 600000) {
                    if (!currentPages.contains(events.get(i).url)) {
                        currentPages.add(events.get(i).url);
                    }
                    i++;
                }

                // update duration
                if (i-1>0) {
                    duration = events.get(i-1).timestamp - startTime;
                }

                sessions.add(new Session(duration, currentPages, startTime));
                currentPages = new ArrayList<>();
                i++;
            }

            // last event
            if (events.get(events.size()-1).timestamp - events.get(events.size()-2).timestamp > 600000) {
                long startTime = events.get(i-1).timestamp;
                String currentPage = events.get(i-1).url;
                currentPages.add(currentPage);
                long duration = 0L;
                sessions.add(new Session(duration, currentPages, startTime));
            }

            sessionMap.put(eventByUserEntry.getKey(), sessions);
        }

        return "result from post request";
    }
}