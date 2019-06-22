package concordia.dems.servers;

import concordia.dems.business.IEventManagerBusiness;
import concordia.dems.business.impl.EventManagerBusinessTorontoImpl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author Loveshant
 * @version 1.0.0
 */
public class TorontoUDPServer implements Runnable {
    private static DatagramSocket aSocket;
    public void run() {
        try{
            aSocket = new DatagramSocket(8889);
            byte[] buffer;
            DatagramPacket request;
            while (true) {
                buffer = new byte[1000];
                System.out.println("Server waiting for request at 8889 ............");
                request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                //byte[] b = new String(request.getData()).getBytes();
                //new Thread(new Responder(b, request.getLength(), request.getAddress(), request.getPort(), aSocket)).start();
                new Thread(new TorontoResponder(request.getData(), request.getLength(), request.getAddress(), request.getPort(), aSocket)).start();
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }

    }

    public static void main(String[] args) {
        new TorontoUDPServer().run();
    }
}

class TorontoResponder implements Runnable {

    DatagramSocket aSocket;
    byte[] data;
    int length;
    InetAddress add;
    int port;
    private static IEventManagerBusiness eventManagerBusinessToronto = new EventManagerBusinessTorontoImpl();

    public TorontoResponder(byte[] data, int length, InetAddress add, int port, DatagramSocket aSocket) {
        //this.request = request;
        this.aSocket = aSocket;
        //reply = new DatagramPacket(data, length, add, port);
        this.data = data;
        this.length = length;
        this.add = add;
        this.port = port;
        //System.out.println("Data :"+ new String(reply.getData()));
    }

    public void  run() {
        DatagramPacket reply;
        String rep;
        String requestString;
        try{
            requestString = new String(data);
            rep = eventManagerBusinessToronto.performOperation(requestString);
            reply = new DatagramPacket(rep.getBytes(), rep.length(), add, port);
            aSocket.send(reply);
        }catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } catch (Exception e) {
            reply = new DatagramPacket("Server Error".getBytes(), "Server Error".length(), add, port);
            try {
                aSocket.send(reply);
            } catch (IOException e1) {
                System.out.println(e);
            }
        }
    }
}

