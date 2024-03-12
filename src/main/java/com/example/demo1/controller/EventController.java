package com.example.demo1.controller;

import com.example.demo1.entities.Event;
import com.example.demo1.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/timestamps")
    public ResponseEntity<List<Event>> searchEventsByTimestamps(@RequestParam List<Long> timestamps) {

        if (timestamps.stream().anyMatch(timestamp -> timestamp <= 0)) {
            throw new IllegalArgumentException("Timestamps cannot contain null values");
        }

        try {
            List<Event> events = eventService.searchEventsByTimestamps(timestamps);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/source/{sourceId}")
    public ResponseEntity<List<Event>> searchEventsBySourceId(@PathVariable int sourceId) {

        if (sourceId < 0) {
            throw new IllegalArgumentException("Source Id must have a value above 0");
        }

        try {
            List<Event> events = eventService.searchEventsBySourceId(sourceId);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/value-range")
    public ResponseEntity<List<Event>> searchEventsByValueRange(@RequestParam double minValue, @RequestParam double maxValue) {

        if (minValue <= 0) {
            throw new IllegalArgumentException("Min Value of range must have a value above 0");
        }
        if (maxValue <= minValue) {
            throw new IllegalArgumentException("Max Value of Range must have a value above Min Value");
        }

        try {
            List<Event> events = eventService.searchEventsByValueRange(minValue, maxValue);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
    }
}

