package concordia.dems.servers;

import concordia.dems.helpers.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author Loveshant
 * @version 1.0.0
 */
public class TorontoUDPClient {
    private static final int montrealServerPort = 8888;
    private static final int ottawaServerPort = 8890;
    public String sendMessageToMontrealUDP(String msg){
        String message = null;
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            byte[] msgBytes = msg.getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(msgBytes, msg.length(), aHost, montrealServerPort);
            aSocket.send(request);
            //System.out.println("Request message sent from the client to server with port number " + serverPort + " is: "+ new String(request.getData()));
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

            aSocket.receive(reply);
            message = new String(reply.getData());
            /*System.out.println("Reply received from the server with port number " + serverPort + " is: "
                    + new String(reply.getData()));*/
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
        return message;
    }

    public String sendMessageToOttawaUDP(String msg){
        String message = null;
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            byte[] msgBytes = msg.getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(msgBytes, msg.length(), aHost, ottawaServerPort);
            aSocket.send(request);
            //System.out.println("Request message sent from the client to server with port number " + serverPort + " is: "+ new String(request.getData()));
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

            aSocket.receive(reply);
            message = new String( reply.getData());
            /*System.out.println("Reply received from the server with port number " + serverPort + " is: "
                    + new String(reply.getData()));*/
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
        return message;
    }

    public String sendMessageToTorontoUDP(String msg){
        String message = null;
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            byte[] msgBytes = msg.getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(msgBytes, msg.length(), aHost, Constants.TORONTOSERVERPORT);
            aSocket.send(request);
            //System.out.println("Request message sent from the client to server with port number " + serverPort + " is: "+ new String(request.getData()));
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

            aSocket.receive(reply);
            message = new String( reply.getData());
            /*System.out.println("Reply received from the server with port number " + serverPort + " is: "
                    + new String(reply.getData()));*/
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
        return message;
    }
}

