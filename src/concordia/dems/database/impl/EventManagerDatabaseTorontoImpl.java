package concordia.dems.database.impl;

import concordia.dems.database.IEventManagerDatabase;
import concordia.dems.helpers.Constants;
import concordia.dems.helpers.Logger;
import concordia.dems.model.Event;
import concordia.dems.model.enumeration.EventType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventManagerDatabaseTorontoImpl implements IEventManagerDatabase {

    private static volatile Map<EventType, Map<String, Event>> eventData = new ConcurrentHashMap<>();
    private static volatile EventManagerDatabaseTorontoImpl eventManagerDatabaseToronto;

    static {
        Map<String, Event> temp = new ConcurrentHashMap<>();
        eventData.put(EventType.CONFERENCE, new ConcurrentHashMap<>());
        eventData.put(EventType.SEMINAR, temp);
        eventData.put(EventType.TRADESHOW, new ConcurrentHashMap<>());
    }

    public static EventManagerDatabaseTorontoImpl getInstance() {
        if (EventManagerDatabaseTorontoImpl.eventManagerDatabaseToronto == null) {
            synchronized (EventManagerDatabaseTorontoImpl.class) {
                if (EventManagerDatabaseTorontoImpl.eventManagerDatabaseToronto == null) {
                    EventManagerDatabaseTorontoImpl.eventManagerDatabaseToronto = new EventManagerDatabaseTorontoImpl();
                }
            }
        }
        return EventManagerDatabaseTorontoImpl.eventManagerDatabaseToronto;
    }

    /**
     * @param event object details
     * @return Boolean True if new event added false otherwise(but booking capacity is updated)
     */
    @Override
    public String addEvent(Event event) {
        if (eventData.get(event.getEventType()).containsKey(event.getEventId())) {
            Event existingEvent = eventData.get(event.getEventType()).get(event.getEventId());
            int updatedRemainingCapacity = Math.abs(existingEvent.getBookingCapacity() - event.getBookingCapacity()) + existingEvent.getRemainingCapacity();
            eventData.get(event.getEventType()).get(event.getEventId()).setRemainingCapacity(updatedRemainingCapacity);
            eventData.get(event.getEventType()).get(event.getEventId()).setBookingCapacity(event.getBookingCapacity());
            Logger.writeLogToFile("server", "torontoServer", "addEvent", "updated :" + event.getEventId(), Constants.TIME_STAMP);
            return ("Event : " + event.getEventId() + " is updated.");
        }
        event.setRemainingCapacity(event.getBookingCapacity());
        eventData.get(event.getEventType()).put(event.getEventId(), event);
        Logger.writeLogToFile("server", "torontoServer", "addEvent", "added : " + event.getEventId(), Constants.TIME_STAMP);
        return ("Event : " + event.getEventId() + " is added.");
    }

    /**
     * @param event object details
     * @return Boolean true if removed and false otherwise
     */
    @Override
    public String removeEvent(Event event) {
        if (eventData.get(event.getEventType()).containsKey(event.getEventId())) {
            eventData.get(event.getEventType()).remove(event.getEventId());
            Logger.writeLogToFile("server", "torontoServer", "removeEvent", "removed : " + event.getEventId(), Constants.TIME_STAMP);
            return ("Event : " + event.getEventId() + " is removed.");
        }
        Logger.writeLogToFile("server", "torontoServer", "removeEvent", "event id not found : " + event.getEventId(), Constants.TIME_STAMP);
        return ("Event : " + event.getEventId() + " not found.");
    }

    /**
     * @param eventType object details
     * @return list of events
     */
    @Override
    public List<Event> listEventAvailability(EventType eventType) {
        List<Event> eventList = new ArrayList<>();
        Iterator iterator = eventData.get(eventType).entrySet().iterator();
        Event e;
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            e = (Event) pair.getValue();
            eventList.add(e);
        }
        Logger.writeLogToFile("server", "torontoServer", "listEventAvailability", "fetch and sent", Constants.TIME_STAMP);
        return eventList;
    }

    /**
     * @param customerID customer id
     * @param eventID    event id
     * @param eventType  event type
     * @return will deduct remaining capacity and add customerID to event customer list
     */
    @Override
    public String bookEvent(String customerID, String eventID, EventType eventType) {
        if (eventData.get(eventType).containsKey(eventID)) {
            if (eventData.get(eventType).get(eventID).getRemainingCapacity() > 0) {
                eventData.get(eventType).get(eventID).addCustomer(customerID);
                eventData.get(eventType).get(eventID).setRemainingCapacity(eventData.get(eventType).get(eventID).getRemainingCapacity() - 1);
                Logger.writeLogToFile("server", "torontoServer", "bookEvent", "event booked for customer " + customerID, Constants.TIME_STAMP);
                return ("Success - Event : " + eventID + " booked for customer : " + customerID);
            } else {
                Logger.writeLogToFile("server", "torontoServer", "bookEvent", "Capacity is full", Constants.TIME_STAMP);
                return ("Rejected - Event : " + eventID + " capacity full");
            }
        }
        Logger.writeLogToFile("server", "torontoServer", "bookEvent", "no such event found for event id " + eventID, Constants.TIME_STAMP);
        return ("Rejected - Event : " + eventID + " not found");
    }

    /**
     * @param customerID customer id
     * @return all events related to customerID
     */
    @Override
    public List<Event> getBookingSchedule(String customerID) {
        List<Event> eventList = new ArrayList<>();
        Iterator it;
        Event e;
        // iterate over enums using for loop
        for (EventType eventType : EventType.values()) {
            it = eventData.get(eventType).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                e = (Event) pair.getValue();
                if (e.getCustomers().contains(customerID))
                    eventList.add(e);
            }
        }
        Logger.writeLogToFile("server", "torontoServer", "getBookingSchedule", "booking schedule sent for customer " + customerID, Constants.TIME_STAMP);
        return eventList;
    }

    /**
     * @param customerID customer id
     * @param eventID    event id
     * @return true when event exist with customer and false if there is no event
     */
    @Override
    public String cancelEvent(String customerID, String eventID) {
        boolean found = false;
        Event eventToCancel = null;
        Event e;
        Iterator it;
        for (EventType eventType : EventType.values()) {
            it = eventData.get(eventType).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                e = (Event) pair.getValue();
                if (e.getEventId().equalsIgnoreCase(eventID)) {
                    eventToCancel = e;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        if (eventToCancel != null && eventToCancel.getCustomers().contains(customerID)) {
            eventToCancel.setRemainingCapacity(eventToCancel.getRemainingCapacity() + 1);
            eventToCancel.removeCustomer(customerID);
            Logger.writeLogToFile("server", "torontoServer", "cancelEvent", "event cancel for " + customerID + " in " + eventID, Constants.TIME_STAMP);
            return ("Success - event cancel for " + customerID + " in " + eventID);
        }
        Logger.writeLogToFile("server", "torontoServer", "cancelEvent", "event cancel rejected for " + customerID + " in " + eventID + " event not found", Constants.TIME_STAMP);
        return "Rejected - customer : " + customerID + " is not booked for event :" + eventID;
    }

    /**
     * @param eventID event id
     * @return remaining capacity of event if exist and -1 if event is not present
     */
    @Override
    public int getRemainingCapacityOfEvent(String eventID) {
        Iterator it;
        Event e;
        for (EventType eventType : EventType.values()) {
            if (eventData.get(eventType).containsKey(eventID)) {
                it = eventData.get(eventType).entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    e = (Event) pair.getValue();
                    if (e.getEventId().equals(eventID)) {
                        return e.getRemainingCapacity();
                    }
                }
            }
        }
        return -1;
    }


}
