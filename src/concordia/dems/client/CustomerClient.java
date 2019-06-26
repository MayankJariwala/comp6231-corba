package concordia.dems.client;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import concordia.dems.communication.IEventManagerCommunication;
import concordia.dems.helpers.Constants;
import concordia.dems.helpers.EventOperation;
import concordia.dems.helpers.Helper;
import concordia.dems.helpers.Logger;
import concordia.dems.helpers.ManagerAndClientInfo;
import concordia.dems.model.ORBServerFactory;
import concordia.dems.model.enumeration.Servers;

/**
 * @author Mayank Jariwala
 * @version 1.0.0
 */
public class CustomerClient {

	private Scanner readInput = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("Welcome to customer application :)");
		CustomerClient customerClient = new CustomerClient();
		customerClient.execute();
	}

	private void execute() {
		String customerId;
		while (true) {
			System.out.print("Enter your id : ");
			customerId = readInput.nextLine();
			String from = Helper.getServerNameFromID(customerId);
			Servers server = Helper.getServerFromId(customerId);
			IEventManagerCommunication communication = ORBServerFactory.getInstance(server);
			String requestBody;
			try {
				listCustomerOperations();
				System.out.print("Select operation id : ");
				int operationID = readInput.nextInt();
				String operationName = ManagerAndClientInfo.clientOperations.get(operationID - 1);
				switch (operationName) {
				case EventOperation.BOOK_EVENT:
					requestBody = from + "," + this.bookEventInformation(customerId);
					String bookEventResponse = communication.performOperation(requestBody);
					System.out.println(bookEventResponse);
					Logger.writeLogToFile("client", customerId, requestBody, bookEventResponse, Constants.TIME_STAMP);
					break;
				case EventOperation.CANCEL_EVENT:
					requestBody = from + "," + this.cancelEventInformation(customerId);
					String cancelEventResponse = communication.performOperation(requestBody);
					System.out.println(cancelEventResponse);
					Logger.writeLogToFile("client", customerId, requestBody, cancelEventResponse, Constants.TIME_STAMP);
					break;
				case EventOperation.GET_BOOKING_SCHEDULE:
					requestBody = from + "," + from + "," + EventOperation.GET_BOOKING_SCHEDULE + "," + customerId;
					String bookingScheduleResponse = communication.performOperation(requestBody);
					System.out.println(bookingScheduleResponse);
					Logger.writeLogToFile("client", customerId, requestBody, bookingScheduleResponse,
							Constants.TIME_STAMP);
					break;
				case EventOperation.SWAP_EVENT:
					requestBody = from + "," + this.swapEvent(customerId);
					String swapEventResponse = communication.performOperation(requestBody);
					System.out.println(swapEventResponse);
					Logger.writeLogToFile("client", customerId, requestBody, swapEventResponse, Constants.TIME_STAMP);
					break;
				}
				readInput.nextLine();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Swap Event Function will allow user to swap the old event with new event
	 * 
	 * @return String Request Body
	 */
	private String swapEvent(String customerID) {
		String requestBody = customerID;
		System.out.print("Enter NEW EVENT ID : ");
		requestBody += "," + readInput.next();
		System.out.print("Enter NEW EVENT Type(SEMINAR/CONFERENCE/TRADESHOW) : ");
		requestBody += "," + readInput.next();
		System.out.print("Enter OLD EVENT ID : ");
		requestBody += "," + readInput.next();
		System.out.print("Enter OLD EVENT Type(SEMINAR/CONFERENCE/TRADESHOW) : ");
		requestBody += "," + readInput.next();
		return " " + "," + EventOperation.SWAP_EVENT + "," + requestBody;
	}

	/**
	 * Ask Certain Information While Booking an Event from User or Manager
	 *
	 * @return String : Request Body
	 */
	private String bookEventInformation(String customerID) {
		String requestBody = customerID;
		System.out.print("Enter EVENT ID : ");
		String eventID = readInput.next();
		String to = Helper.getServerNameFromID(eventID);
		requestBody += "," + eventID;
		System.out.print("Enter EVENT Type(SEMINAR/CONFERENCE/TRADESHOW) : ");
		requestBody += "," + readInput.next();
		return to + "," + EventOperation.BOOK_EVENT + "," + requestBody;
	}

	private String cancelEventInformation(String customerId) {
		String body = customerId;
		System.out.print("Enter Event ID : ");
		String eventID = readInput.next();
		body += "," + eventID;
		String to = Helper.getServerNameFromID(eventID);
		return to + "," + EventOperation.CANCEL_EVENT + "," + body;
	}

	private void listCustomerOperations() {
		AtomicInteger idCounter = new AtomicInteger(1);
		System.out.println("Select number from below option for performing any operation");
		ManagerAndClientInfo.clientOperations
				.forEach(clientId -> System.out.println(idCounter.getAndIncrement() + " " + clientId));
	}
}
