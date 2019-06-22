package concordia.dems.business;

import concordia.dems.model.Event;

import java.util.List;

/**
 * NOTE: Event each info is received in csv format
 *
 * @author Mayank Jariwala
 * @version 1.0.0
 */
public interface IEventManagerBusiness {

    /**
     * Add Event function: Accessible by Event Manager
     *
     * @param addEventInfo: The information about event provided by Manager
     * @return boolean Success or failure
     */
    //addEvent : eventID, eventType, bookingCapacity
    String addEvent(String addEventInfo);

    /**
     * Remove Event function: Accessible by Event Manager
     *
     * @param removeEventInfo: The information required to remove an event
     * @return boolean Success or failure
     */
    //removeEvent : eventId,eventType
    String removeEvent(String removeEventInfo);

    /**
     * This function returns how much space is available in requested eventType
     *
     * @param eventType: Type of Event [Conference,Seminar,Trade off]
     * @return List<Event>
     */
    // listEventAvailability: eventType
    List<Event> listEventAvailability(String eventType);

    /**
     * @param eventBookingInfo: Event Booking
     * @return
     */
    //bookEvent : String eventBookingInfo [customerId,eventId,eventType]
    String bookEvent(String eventBookingInfo);

    /**
     * This function returns an booking schedule of requested customer
     *
     * @param customerID: Unique Identifier of Customer
     * @return List<Event> Returns List of Event customer has registered
     */
    // getBookingSchedule: String customerId
    List<Event> getBookingSchedule(String customerID);

    /**
     * @param cancelEventInfo: Information required to cancel an event
     * @return boolean Success or Failure
     */
    //cancelEvent: customerId,eventId
    String cancelEvent(String cancelEventInfo);

    String performOperation(String userRequest);
}
