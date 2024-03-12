package com.example.demo1.services;

import com.example.demo1.data.DataLoader;
import com.example.demo1.entities.Event;
import com.example.demo1.entities.Source;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    private List<Event> events;

    Map<Long, List<Event>> timestampEventMap = new HashMap<>();

    @PostConstruct
    public void init() {
        List<Source> sourceList = DataLoader.loadSourcesFromFile("src/main/resources/sources.txt");
        this.events = DataLoader.loadEventsFromFile("src/main/resources/events.txt", sourceList);
        this.timestampEventMap = events.stream()
                .collect(Collectors.groupingBy(Event::getTimestamp));
    }

    public List<Event> searchEventsByTimestamps(List<Long> timestamps) {
        List<Event> result = new ArrayList<>();
        timestamps.forEach(timestamp ->
                result.addAll(timestampEventMap.getOrDefault(timestamp, Collections.emptyList()))
        );
        return result;
    }

    public List<Event> searchEventsBySourceId(int sourceId) {
        return events.stream()
                .filter(e -> e.getSource().getId() == sourceId)
                .toList();
    }

    public List<Event> searchEventsByValueRange(double minValue, double maxValue) {
        return events.stream()
                .filter(e -> e.getValue() >= minValue && e.getValue() <= maxValue)
                .toList();
    }
}
