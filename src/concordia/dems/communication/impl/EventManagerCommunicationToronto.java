package concordia.dems.communication.impl;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import concordia.dems.communication.IEventManagerCommunication;
import concordia.dems.communication.IEventManagerCommunicationHelper;
import concordia.dems.communication.IEventManagerCommunicationPOA;
import concordia.dems.helpers.Constants;
import concordia.dems.helpers.EventOperation;
import concordia.dems.helpers.Helper;
import concordia.dems.servers.TorontoUDPClient;

public class EventManagerCommunicationToronto extends IEventManagerCommunicationPOA {

	private TorontoUDPClient torontoUDPClient;
	private ORB orb = null;

	protected EventManagerCommunicationToronto() {
		super();
		torontoUDPClient = new TorontoUDPClient();
	}

	private void setOrb(ORB orb_val) {
		orb = orb_val;
	}

	public static void main(String[] args) {
		try {
			Properties props = new Properties();
			props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			props.put("org.omg.CORBA.ORBInitialPort", 1050);
			// create and initialize the ORB //
			ORB orbObj = ORB.init(args, props);

			// get reference to rootpoa &amp; activate
			POA rootPoa = POAHelper.narrow(orbObj.resolve_initial_references("RootPOA"));
			rootPoa.the_POAManager().activate();

			// create servant and register it with the ORB
			EventManagerCommunicationToronto communication = new EventManagerCommunicationToronto();
			communication.setOrb(orbObj);
			org.omg.CORBA.Object ref = rootPoa.servant_to_reference(communication);

			// and cast the reference to a CORBA reference
			IEventManagerCommunication communicationToronto = IEventManagerCommunicationHelper.narrow(ref);
			// get the root naming context
			// NameService invokes the transient name service
			org.omg.CORBA.Object objRef = orbObj.resolve_initial_references("NameService");
			// Use NamingContextExt, which is part of the
			// Interoperable Naming Service (INS) specification.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// bind the Object Reference in Naming
			NameComponent path[] = ncRef.to_name("abc");
			ncRef.rebind(path, communicationToronto);
			System.out.println("Addition Server ready and waiting ...");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Server is not started properly");
		}
	}

	/**
	 * @param userRequest: Either Manager or Customer
	 */
	@Override
	public String performOperation(String userRequest) {
		// Checking whether string is empty
		String verifyingRequestBody = userRequest.replaceAll(",", "");
		if (verifyingRequestBody.equals("")) {
			return "The request body is empty";
		}
		String[] unWrappingRequest = userRequest.split(",", 4);

		// If request is for event availability then simply returns all events in server
		if (unWrappingRequest[Constants.ACTION_INDEX].equals(EventOperation.LIST_AVAILABILITY)) {
			return getEventAvailabilityFromAllServers(userRequest);
		}

		// For getting booking schedule of user , also call all servers service
		if (unWrappingRequest[Constants.ACTION_INDEX].equals(EventOperation.GET_BOOKING_SCHEDULE)) {
			return getBookingScheduleForClients(userRequest);
		}

		switch (unWrappingRequest[Constants.TO_INDEX]) {
		case "montreal":
			if (unWrappingRequest[Constants.ACTION_INDEX].equals(EventOperation.BOOK_EVENT)) {
				unWrappingRequest[Constants.ACTION_INDEX] = EventOperation.GET_BOOKING_SCHEDULE;
				boolean isNotEligible = isCustomerEligibleForBookingEvent(unWrappingRequest);
				if (isNotEligible)
					return "Limit Exceeded! You have been already registered for 3 events for a specific month";
				return torontoUDPClient.sendMessageToMontrealUDP(userRequest);
			} else
				return torontoUDPClient.sendMessageToMontrealUDP(userRequest);

		case "toronto":
			return torontoUDPClient.sendMessageToTorontoUDP(userRequest);

		case "ottawa":
			if (unWrappingRequest[Constants.ACTION_INDEX].equals(EventOperation.BOOK_EVENT)) {
				unWrappingRequest[Constants.ACTION_INDEX] = EventOperation.GET_BOOKING_SCHEDULE;
				boolean isNotEligible = isCustomerEligibleForBookingEvent(unWrappingRequest);
				if (isNotEligible)
					return "Limit Exceeded! You have been already registered for 3 events for a specific month";
				return torontoUDPClient.sendMessageToOttawaUDP(userRequest);
			} else
				return torontoUDPClient.sendMessageToOttawaUDP(userRequest);
		}
		return "";
	}

	private String generateStringForUnwrappingRequest(String[] unWrappingRequest) {
		return String.join(",", unWrappingRequest[0], unWrappingRequest[1], unWrappingRequest[2], unWrappingRequest[3]);
	}

	private String getEventAvailabilityFromAllServers(String userRequest) {
		String torontoEvents = torontoUDPClient.sendMessageToTorontoUDP(userRequest);
		String ottawaEvents = torontoUDPClient.sendMessageToOttawaUDP(userRequest);
		String montrealEvents = torontoUDPClient.sendMessageToMontrealUDP(userRequest);
		return String.join("\n", torontoEvents, ottawaEvents, montrealEvents);
	}

	private String getBookingScheduleForClients(String userRequest) {
		String torontoEvents = torontoUDPClient.sendMessageToTorontoUDP(userRequest);
		String ottawaEvents = torontoUDPClient.sendMessageToOttawaUDP(userRequest);
		String montrealEvents = torontoUDPClient.sendMessageToMontrealUDP(userRequest);
		return String.join("\n", torontoEvents, ottawaEvents, montrealEvents);
	}

	private boolean isCustomerEligibleForBookingEvent(String[] unWrappingRequest) {
		String ottawaEvents = torontoUDPClient
				.sendMessageToOttawaUDP(generateStringForUnwrappingRequest(unWrappingRequest));
		String montrealEvents = torontoUDPClient
				.sendMessageToMontrealUDP(generateStringForUnwrappingRequest(unWrappingRequest));
		return Helper.checkIfEqualMoreThanThree(ottawaEvents, montrealEvents,
				unWrappingRequest[Constants.INFORMATION_INDEX]);
	}

}
