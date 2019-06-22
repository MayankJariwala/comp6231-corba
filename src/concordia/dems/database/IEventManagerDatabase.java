package concordia.dems.database;

import concordia.dems.model.Event;
import concordia.dems.model.enumeration.EventType;

import java.util.List;

/**
 * @author Loveshant
 * @version 1.0.0
 */
public interface IEventManagerDatabase {
    String addEvent(Event event);

    String removeEvent(Event event);

    List<Event> listEventAvailability(EventType eventType);

    String bookEvent(String customerID, String eventID, EventType eventType);

    List<Event> getBookingSchedule(String customerID);

    String cancelEvent(String customerID, String eventID);

    int getRemainingCapacityOfEvent(String eventID);
}
