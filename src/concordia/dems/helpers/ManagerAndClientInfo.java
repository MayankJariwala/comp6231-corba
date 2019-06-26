package concordia.dems.helpers;

import java.util.Arrays;
import java.util.List;

/**
 * This Class is having dummy information which is use for running an
 * application
 *
 * @author Mayank Jariwala
 * @version 1.0.0
 */
public class ManagerAndClientInfo {

	// Dummy Manager Ids
	public static final List<String> managersId = Arrays.asList("TORM2345", "MTLM1234", "OTWM1243", "TORM1123");
	// Dummy Client Ids
	public static final List<String> clientsId = Arrays.asList("TORC2345", "MTLC1234", "OTWC1243", "TORC1123");

	// Operations Clients can perform
	public static final List<String> clientOperations = Arrays.asList(EventOperation.BOOK_EVENT,
			EventOperation.GET_BOOKING_SCHEDULE, EventOperation.CANCEL_EVENT, EventOperation.SWAP_EVENT);

	// Operations Manager can perform
	public static final List<String> managerOperations = Arrays.asList(EventOperation.ADD_EVENT,
			EventOperation.REMOVE_EVENT, EventOperation.LIST_AVAILABILITY, EventOperation.BOOK_EVENT,
			EventOperation.GET_BOOKING_SCHEDULE, EventOperation.CANCEL_EVENT);
}
