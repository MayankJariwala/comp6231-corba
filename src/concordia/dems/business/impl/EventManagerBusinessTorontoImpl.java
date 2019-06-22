package concordia.dems.business.impl;

import concordia.dems.business.IEventManagerBusiness;
import concordia.dems.database.IEventManagerDatabase;
import concordia.dems.database.impl.EventManagerDatabaseTorontoImpl;
import concordia.dems.helpers.Constants;
import concordia.dems.helpers.EventOperation;
import concordia.dems.helpers.Helper;
import concordia.dems.helpers.Logger;
import concordia.dems.model.Event;
import concordia.dems.model.enumeration.EventType;

import java.util.List;

/**
 * @author Mayank Jariwala
 * @version 1.0.0
 */
public class EventManagerBusinessTorontoImpl implements IEventManagerBusiness {

    private static IEventManagerDatabase iEventManagerDatabase;

    public EventManagerBusinessTorontoImpl() {
        iEventManagerDatabase = EventManagerDatabaseTorontoImpl.getInstance();
    }

    /*
       Manager Related Functions
    */

    @Override
    public synchronized String addEvent(String addEventInfo) {
        String[] unWrappingRequest = addEventInfo.split(",");
        //0 = EventID , 1 = Event Type , 2 = Event Batch , 3 = Booking Capacity
        Event event = new Event(unWrappingRequest[0].trim(),
                Helper.getEventTypeEnumObject(unWrappingRequest[1].trim()),
                Helper.getEventBatchEnumObject(unWrappingRequest[2].trim()),
                Integer.parseInt(unWrappingRequest[3].trim()));
        return iEventManagerDatabase.addEvent(event);
    }

    @Override
    public synchronized String removeEvent(String removeEventInfo) {
        String[] unWrappingRequest = removeEventInfo.split(",");
        Event e = new Event(unWrappingRequest[0], Helper.getEventTypeEnumObject(unWrappingRequest[1].trim()));
        return iEventManagerDatabase.removeEvent(e);
    }

    @Override
    public synchronized List<Event> listEventAvailability(String eventType) {
        EventType eventTypeObj = Helper.getEventTypeEnumObject(eventType.trim());
        return iEventManagerDatabase.listEventAvailability(eventTypeObj);
    }

    /*
        Customer Related Functions
     */

    @Override
    public synchronized String bookEvent(String eventBookingInfo) {
        String[] unWrappingRequest = eventBookingInfo.split(",");
        String customerID = unWrappingRequest[0].trim();
        String eventID = unWrappingRequest[1].trim();
        EventType eventType = Helper.getEventTypeEnumObject(unWrappingRequest[2].trim());
        return iEventManagerDatabase.bookEvent(customerID, eventID, eventType);
    }

    @Override
    public synchronized List<Event> getBookingSchedule(String customerID) {
        customerID = customerID.split(",")[0].trim();
        return iEventManagerDatabase.getBookingSchedule(customerID);
    }

    @Override
    public synchronized String cancelEvent(String cancelEventInfo) {
        String[] unWrappingRequest = cancelEventInfo.split(",");
        String customerID = unWrappingRequest[0].trim();
        String eventID = unWrappingRequest[1].trim();
        return iEventManagerDatabase.cancelEvent(customerID, eventID);
    }

    @Override
    public String performOperation(String userRequest) {
        String[] unWrappingRequest = userRequest.split(",", 4);
        switch (unWrappingRequest[Constants.ACTION_INDEX]) {
            case EventOperation.BOOK_EVENT:
                return this.bookEvent(unWrappingRequest[Constants.INFORMATION_INDEX]);
            case EventOperation.CANCEL_EVENT:
                return this.cancelEvent(unWrappingRequest[Constants.INFORMATION_INDEX]);
            case EventOperation.GET_BOOKING_SCHEDULE:
                List<Event> bookingSchedule = this.getBookingSchedule(unWrappingRequest[Constants.INFORMATION_INDEX]);
                StringBuilder bookingInformation = new StringBuilder();
                for (Event e : bookingSchedule) {
                    bookingInformation.append(e.getEventId()).append(" ").append(e.getEventType()).append(" ").append(e.getBookingCapacity()).append(" ").append(e.getRemainingCapacity()).append("\n");
                }
                return bookingInformation.toString();
            case EventOperation.ADD_EVENT:
                if (isManagerAllowToPerformEventOperation(unWrappingRequest)) {
                    Logger.writeLogToFile("server", "non-authorized", unWrappingRequest[Constants.INFORMATION_INDEX], "You are not authorized to add an event", Constants.TIME_STAMP);
                    return "You are not authorized to add an event";
                }
                return this.addEvent(unWrappingRequest[Constants.INFORMATION_INDEX]);
            case EventOperation.REMOVE_EVENT:
                if (isManagerAllowToPerformEventOperation(unWrappingRequest)) {
                    Logger.writeLogToFile("server", "non-authorized", unWrappingRequest[Constants.INFORMATION_INDEX], "You are not authorized to remove an event", Constants.TIME_STAMP);
                    return "You are not authorized to remove an event";
                }
                return this.removeEvent(unWrappingRequest[Constants.INFORMATION_INDEX]);
            case EventOperation.LIST_AVAILABILITY:
                List<Event> eventList = this.listEventAvailability(unWrappingRequest[Constants.INFORMATION_INDEX]);
                StringBuilder eventAvailabilityInformation = new StringBuilder();
                for (Event e : eventList) {
                    eventAvailabilityInformation.append(e.getEventId()).append(" ").append(e.getEventType()).append(" ").append(e.getBookingCapacity()).append(" ").append(e.getRemainingCapacity()).append("\n");
                }
                return eventAvailabilityInformation.toString();
            default:
                break;
        }
        return "";
    }

    private boolean isManagerAllowToPerformEventOperation(String[] unwrappingRequest) {
        return !unwrappingRequest[Constants.FROM_INDEX].equals(unwrappingRequest[Constants.TO_INDEX]);
    }
}
