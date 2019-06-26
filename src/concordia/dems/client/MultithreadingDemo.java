package concordia.dems.client;

import concordia.dems.communication.IEventManagerCommunication;
import concordia.dems.helpers.EventOperation;
import concordia.dems.model.ORBServerFactory;
import concordia.dems.model.enumeration.EventBatch;
import concordia.dems.model.enumeration.EventType;
import concordia.dems.model.enumeration.Servers;

/**
 * This class is just to represent the multithreading demo
 *
 * @author MayankJariwala
 * @version 1.0.0
 */
public class MultithreadingDemo {

	private IEventManagerCommunication iEventManagerCommunication;

	public static void main(String[] args) {
		MultithreadingDemo multithreadingDemo = new MultithreadingDemo();
		multithreadingDemo.executeThreadOperations();
	}

	private void executeThreadOperations() {
		iEventManagerCommunication = ORBServerFactory.getInstance(Servers.MONTREAL);
		Runnable manager = () -> {
			String requestBody = "montreal,montreal," + EventOperation.ADD_EVENT + ",MTLA181019," + EventType.SEMINAR
					+ "," + EventBatch.AFTERNOON + "," + 2;
			String response = iEventManagerCommunication.performOperation(requestBody);
			System.out.println("Response for Event Added by Manager : " + response);
		};

		Runnable manager1 = () -> {
			String requestBody = "montreal,montreal," + EventOperation.REMOVE_EVENT + ",MTLA181019,"
					+ EventType.SEMINAR;
			String response = iEventManagerCommunication.performOperation(requestBody);
			System.out.println("Response for Event Removed by Manager : " + response);
		};

		Runnable client1 = () -> {
			String requestBody = "montreal,montreal," + EventOperation.BOOK_EVENT + ",MTLC1234,MTLA181019,"
					+ EventType.SEMINAR;
			String response = iEventManagerCommunication.performOperation(requestBody);
			System.out.println("Response for Event Booked by MTLC1234 [Client1] : " + response);
		};

		Runnable client2 = () -> {
			String requestBody = "montreal,montreal," + EventOperation.BOOK_EVENT + ",MTLC2234,MTLA181019,"
					+ EventType.SEMINAR;
			String response = iEventManagerCommunication.performOperation(requestBody);
			System.out.println("Response for Event Booked by MTLC2234 [Client2] : " + response);
		};

		Runnable client3 = () -> {
			String requestBody = "montreal,montreal," + EventOperation.BOOK_EVENT + ",MTLC3234,MTLA181019,"
					+ EventType.SEMINAR;
			String response = iEventManagerCommunication.performOperation(requestBody);
			System.out.println("Response for Event Booked by MTLC3234 [Client3] : " + response);
		};

		Runnable client4 = () -> {
			String requestBody = "montreal,montreal," + EventOperation.BOOK_EVENT + ",MTLC9234,MTLA181019,"
					+ EventType.SEMINAR;
			String response = iEventManagerCommunication.performOperation(requestBody);
			System.out.println("Response for Event Booked by MTLC9234 [Client4] : " + response);
		};

		Runnable client5 = () -> {
			String requestBody = "montreal,montreal," + EventOperation.BOOK_EVENT + ",MTLC7234,MTLA181019,"
					+ EventType.SEMINAR;
			String response = iEventManagerCommunication.performOperation(requestBody);
			System.out.println("Response for Event Booked by MTLC7234 [Client5] : " + response);
		};

		// Start all threads
		Thread thread = new Thread(manager);
		Thread threadManager = new Thread(manager1);
		Thread thread1 = new Thread(client1);
		Thread thread2 = new Thread(client2);
		Thread thread3 = new Thread(client3);
		Thread thread4 = new Thread(client4);
		Thread thread5 = new Thread(client5);
		thread.start();
		threadManager.start();
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		thread5.start();
	}
}
