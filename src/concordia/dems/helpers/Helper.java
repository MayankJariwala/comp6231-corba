package concordia.dems.helpers;

import concordia.dems.model.enumeration.EventBatch;
import concordia.dems.model.enumeration.EventType;
import concordia.dems.model.enumeration.Servers;

public class Helper {

    /**
     * This function returns which city this client belongs to
     *
     * @param id: Customer Id or Manager Id
     * @return Servers
     */
    public static Servers getServerFromId(String id) {
        Servers server = null;
        String serverPrefix = id.substring(0, 3).toLowerCase();
        switch (serverPrefix) {
            case "mtl":
                server = Servers.MONTREAL;
                break;
            case "otw":
                server = Servers.OTTAWA;
                break;
            case "tor":
                server = Servers.TORONTO;
                break;
        }
        return server;
    }

    /**
     * Returns Enumeration Type of String by receiving string as parameter
     *
     * @param eventType: Type of Event
     * @return String
     */
    public static EventType getEventTypeEnumObject(String eventType) {
        EventType type = null;
        eventType = eventType.toUpperCase();
        switch (eventType) {
            case "CONFERENCE":
                type = EventType.CONFERENCE;
                break;
            case "SEMINAR":
                type = EventType.SEMINAR;
                break;
            case "TRADESHOW":
                type = EventType.TRADESHOW;
                break;
            default:
                break;
        }
        return type;
    }


    /**
     * Returns Enumeration Type of EventBatch by receiving char as parameter
     *
     * @param eventBatch: Batch/Time Slot of Event
     * @return EventBatch
     */
    public static EventBatch getEventBatchEnumObject(String eventBatch) {
        EventBatch batch = null;
        switch (eventBatch) {
            case "AFTERNOON":
                batch = EventBatch.AFTERNOON;
                break;
            case "EVENING":
                batch = EventBatch.EVENING;
                break;
            case "MORNING":
                batch = EventBatch.MORNING;
                break;
            default:
                break;
        }
        return batch;
    }

    // This function is use to add parameter like from,to into user request body
    public static String getServerNameFromID(String ID) {
        String server = "";
        String serverPrefix = ID.substring(0, 3).toLowerCase();
        switch (serverPrefix) {
            case "mtl":
                server = "montreal";
                break;
            case "otw":
                server = "ottawa";
                break;
            case "tor":
                server = "toronto";
                break;
        }
        return server;
    }

    public static boolean checkIfEqualMoreThanThree(String events1, String events2, String inf) {
        //get month of current booking
        String currMonth = inf.split(",")[1].substring(6, 8).trim();
        int eventCount = 0;

        String[] events = events1.split("\n");
        for (String s : events) {
            if (currMonth.equalsIgnoreCase(s.split(" ")[0].substring(6, 8).trim())) {
                eventCount++;
            }
        }
        events = events2.split("\n");
        for (String s : events) {
            if (currMonth.equalsIgnoreCase(s.split(" ")[0].substring(6, 8).trim())) {
                eventCount++;
            }
        }
        return eventCount >= 3;
    }
}
