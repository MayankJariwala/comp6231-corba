package concordia.dems.client;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import concordia.dems.communication.IEventManagerCommunication;
import concordia.dems.helpers.Constants;
import concordia.dems.helpers.EventOperation;
import concordia.dems.helpers.Helper;
import concordia.dems.helpers.Logger;
import concordia.dems.helpers.ManagerAndClientInfo;
import concordia.dems.model.RMIServerFactory;
import concordia.dems.model.enumeration.EventType;
import concordia.dems.model.enumeration.Servers;

/**
 * NOTE: Request format body parameter:
 * actionName,from,to,eventInformation/needed information
 *
 * @author Mayank Jariwala
 */
public class ManagerClient {

	private IEventManagerCommunication iEventManagerCommunication;
	private Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("Welcome to manager application :)");
		ManagerClient managerClient = new ManagerClient();
		managerClient.execute();
	}

	private void execute() {
		String customerId, eventInfo, response, requestBody;
		System.err.print("Enter your id : ");
		String managerID = scanner.nextLine();
		Servers servers = Helper.getServerFromId(managerID);
		String from = Helper.getServerNameFromID(managerID);
		iEventManagerCommunication = RMIServerFactory.getInstance(servers);
		while (true) {
			showManagerOperations();
			int operationID = scanner.nextInt();
			String operationName = ManagerAndClientInfo.managerOperations.get(operationID - 1);
			try {
				switch (operationName) {
				case EventOperation.ADD_EVENT:
					String eventInformation = createNewEvent();
					requestBody = from + "," + eventInformation;
					response = iEventManagerCommunication.performOperation(requestBody);
					Logger.writeLogToFile("client", managerID, requestBody, response, Constants.TIME_STAMP);
					System.out.println(response);
					break;
				case EventOperation.REMOVE_EVENT:
					requestBody = from + "," + removeEventInformation();
					response = iEventManagerCommunication.performOperation(requestBody);
					Logger.writeLogToFile("client", managerID, requestBody, response, Constants.TIME_STAMP);
					System.out.println(response);
					break;
				case EventOperation.LIST_AVAILABILITY:
					System.err.print("Enter event type: ");
					eventInfo = scanner.next();
					// to=from [ Compulsory get results from all server]
					requestBody = from + "," + from + "," + EventOperation.LIST_AVAILABILITY + "," + eventInfo;
					String listEventResponse = iEventManagerCommunication.performOperation(requestBody);
					Logger.writeLogToFile("client", managerID, requestBody, listEventResponse, Constants.TIME_STAMP);
					System.out.println(listEventResponse);
					break;
				// Manager can perform operation for client
				case EventOperation.BOOK_EVENT:
					requestBody = from + "," + bookEventInformation();
					response = iEventManagerCommunication.performOperation(requestBody);
					System.out.println(response);
					Logger.writeLogToFile("client", managerID, requestBody, response, Constants.TIME_STAMP);
					break;
				case EventOperation.CANCEL_EVENT:
					requestBody = from + "," + cancelEventInformation();
					response = iEventManagerCommunication.performOperation(requestBody);
					System.out.println(response);
					Logger.writeLogToFile("client", managerID, requestBody, response, Constants.TIME_STAMP);
					break;
				case EventOperation.GET_BOOKING_SCHEDULE:
					System.out.print("Enter your client ID : ");
					customerId = scanner.next();
					String to = Helper.getServerNameFromID(customerId);
					requestBody = from + "," + to + "," + EventOperation.GET_BOOKING_SCHEDULE + "," + customerId;
					response = iEventManagerCommunication.performOperation(requestBody);
					System.out.println(response);
					Logger.writeLogToFile("client", managerID, requestBody, response, Constants.TIME_STAMP);
					break;
				}
			} catch (Exception e) {
				System.err.print("Exception message " + e.getMessage());
			}
		}
	}

	private void showManagerOperations() {
		AtomicInteger idCounter = new AtomicInteger(1);
		System.out.println("Select operation id from below option : ");
		ManagerAndClientInfo.managerOperations
				.forEach(managerOperation -> System.out.println(idCounter.getAndIncrement() + " " + managerOperation));
	}

	/**
	 * Ask question regarding new event from manager
	 *
	 * @return String
	 */
	private String createNewEvent() {
		String newEventInfo;
		System.err.print("Enter event City (MTL,TOR,OTW) : ");
		String eventCity = scanner.next().toUpperCase();
		System.err.print("Enter event Batch (A/M/E): ");
		String eventBatch = scanner.next().toUpperCase();
		System.err.print("Enter event date (ddmmyy): ");
		String eventDate = scanner.next().toUpperCase();
		String eventID = eventCity + "" + eventBatch + "" + eventDate;
		System.out.println("Enter event Type from below list : ");
		Arrays.stream(EventType.values()).forEach(System.out::println);
		String eventType = scanner.next();
		System.err.print("Enter event booking capacity : ");
		int bookingCapacity = scanner.nextInt();
		System.out.println("Generated Event ID is : " + eventID);
		String to = Helper.getServerNameFromID(eventID);
		newEventInfo = to + "," + EventOperation.ADD_EVENT + "," + eventID + "," + eventType + "," + eventBatch + ","
				+ bookingCapacity;
		return newEventInfo;
	}

	/**
	 * Ask Certain Information While Booking an Event from User or Manager
	 *
	 * @return String : Request Body
	 */
	private String bookEventInformation() {
		String requestBody = "";
		System.err.print("Enter Customer ID : ");
		requestBody += scanner.next();
		System.err.print("Enter EVENT ID : ");
		String eventID = scanner.next();
		String to = Helper.getServerNameFromID(eventID);
		requestBody += "," + eventID;

		System.err.print("Enter EVENT Type(SEMINAR/CONFERENCE/TRADESHOW) : ");
		requestBody += "," + scanner.next();
		return to + "," + EventOperation.BOOK_EVENT + "," + requestBody;
	}

	private String removeEventInformation() {
		String removeEventInfo = "";
		System.err.print("Enter event id: ");
		String eventID = scanner.next();
		removeEventInfo += eventID;
		String to = Helper.getServerNameFromID(eventID);
		System.err.print("Enter event type: ");
		removeEventInfo += "," + scanner.next().toUpperCase();
		return to + "," + EventOperation.REMOVE_EVENT + "," + removeEventInfo;
	}

	private String cancelEventInformation() {
		String body = "";
		System.out.print("Enter your client ID : ");
		body += scanner.next();
		System.out.print(" Enter Event ID : ");
		String eventID = scanner.next();
		body += "," + eventID;
		String to = Helper.getServerNameFromID(eventID);
		return to + "," + EventOperation.CANCEL_EVENT + "," + body;
	}
}
