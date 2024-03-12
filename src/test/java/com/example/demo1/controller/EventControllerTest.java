package com.example.demo1.controller;

import com.example.demo1.entities.Event;
import com.example.demo1.entities.Source;
import com.example.demo1.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class EventControllerTest {



    private EventService eventService;

    private EventController eventController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        eventService = new EventService();
        eventService.init();
        eventController = new EventController(eventService); // Inject EventService into EventController
    }

    @Test
    public void testSearchEventsByNonExistentTimestamps() {
        List<Long> timestamps = Arrays.asList(1615891240L, 1615892420L);
        List<Event> expectedEvents = Collections.emptyList();

        ResponseEntity<List<Event>> responseEntity = eventController.searchEventsByTimestamps(timestamps);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedEvents, responseEntity.getBody());
    }

    @Test
    public void testSearchEventsByExistentTimestamps() {
        List<Long> timestamps = Arrays.asList(1615909200L, 1615911600L);
        Event evt1 = Event.createEvent()
                .setId(16)
                .setTimestamp(1615909200)
                .setValue(19.7)
                .setSource(Source.createSource().setId(1).setName("Source A")
                );
        Event evt2 = Event.createEvent()
                .setId(17)
                .setTimestamp(1615909200)
                .setValue(11.6)
                .setSource(Source.createSource().setId(2).setName("Source B")
                );
        Event evt3 = Event.createEvent()
                .setId(18)
                .setTimestamp(1615911600)
                .setValue(15.3)
                .setSource(Source.createSource().setId(3).setName("Source C")
                );
        List<Event> expectedEvents = Arrays.asList(evt1, evt2, evt3);

        ResponseEntity<List<Event>> responseEntity = eventController.searchEventsByTimestamps(timestamps);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedEvents, responseEntity.getBody());
    }


    @Test
    void testSearchEventsByValueRangeFailNegativeNumber() {
        double minValue = -10.0;
        double maxValue = 20.0;

        assertThrows(IllegalArgumentException.class, () ->
                eventController.searchEventsByValueRange(minValue, maxValue));
    }

    @Test
    void testSearchEventsByValueRangeFailInvalidRange() {
        double minValue = 10.0;
        double maxValue = 2.0;
        List<Event> expectedEvents = Arrays.asList(new Event(), new Event());

        assertThrows(IllegalArgumentException.class, () ->
                eventController.searchEventsByValueRange(minValue, maxValue));
    }


    @Test
    void testSearchEventsByValueRange() {
        double minValue = 16.0;
        double maxValue = 17.0;
        Event evt = Event.createEvent()
                .setId(8)
                .setTimestamp(1615899600)
                .setValue(16.4)
                .setSource(Source.createSource().setId(3).setName("Source C")
                );
        Event evt2 = Event.createEvent()
                .setId(22)
                .setTimestamp(1615916400)
                .setValue(16.9)
                .setSource(Source.createSource().setId(2).setName("Source B")
                );
        List<Event> expectedEvents = List.of(evt, evt2);
        ResponseEntity<List<Event>> responseEntity = eventController.searchEventsByValueRange(minValue, maxValue);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedEvents, responseEntity.getBody());
    }

}