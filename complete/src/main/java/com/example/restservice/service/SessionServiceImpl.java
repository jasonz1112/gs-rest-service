package com.example.restservice.service;

//import org.json.JSONArray;

import com.example.restservice.model.Event;
import com.example.restservice.model.EventWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SessionServiceImpl implements SessionService {

    private String getEventsUrl = "https://candidate.hubteam.com/candidateTest/v3/problem/dataset?userKey=297df2ff1740b5aa8a3975b1ae2b";

    @Override
    public List<Event> getEvents() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        EventWrapper eventWrapper = restTemplate.getForObject(getEventsUrl, EventWrapper.class);
        return eventWrapper.getEvents();
    }


}