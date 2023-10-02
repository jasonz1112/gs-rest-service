package com.example.restservice.service;

import com.example.restservice.model.Event;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SessionService {

    List<Event> getEvents() throws IOException;

}