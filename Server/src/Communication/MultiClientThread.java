package Communication;

import CollectionCLI.CollectionHandler;
import Plot.Event;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

import static Communication.Protocol.time;

public class MultiClientThread extends Thread {    private Socket socket = null;
    CollectionHandler ch;



    public MultiClientThread(Socket socket, CollectionHandler ch) {
        super("MultiClientThread");
        this.socket = socket;
        this.ch = ch;
    }


    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            String inputLine = "", outputLine;
            Protocol protocol = new Protocol(ch);
            outputLine = "Connected: " + socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort() + "." + socket.getPort();
            System.out.println("Client connected " + socket.getPort());
            out.writeObject(outputLine);
            out.flush();
            ObjectInputStream in = new ObjectInputStream(
                    socket.getInputStream());
            Emoji.playGame(in, out);
            time.resetTime();
            do {
                out.writeObject("Enter a command:");
                out.flush();
                try {
                    ArrayList<String> cmd = (ArrayList<String>) in.readObject();
                    StringBuilder s = new StringBuilder();
                    cmd.forEach((p) -> s.append(p).append(" "));
                    inputLine = s.toString();
                    System.out.println(socket.getPort() + ": " + inputLine);
                    if (cmd.get(0).equals("generate")) {

                        out.writeObject("generating");
                        out.flush();

                        out.writeObject(ch.Events);
                        out.flush();

                    } else {
                        if (CollectionHandler.objComms.contains(cmd.get(0))) {
                            outputLine = protocol.processResponse(cmd, (Event) in.readObject());
                        } else {
                            outputLine = protocol.processResponse(cmd, null);
                        }
                        out.writeObject(outputLine);
                        out.flush();
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found");
                } catch (SocketException e) {
                    System.out.println("Somebody killed the connection. Who in the world it might be? \uD83E\uDD14" +
                            Emoji.thinking);
                } catch (NullPointerException e) {
                    System.out.println("NPE thrown");
                }
            } while (!inputLine.trim().equals("quit"));
        } catch (IOException e) {
            System.out.println("Connection killed violently");
        }
        System.out.println(socket.getPort() + " disconnected.");
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Socket is not fine already, don't hurt it anymore.");
        }
    }
}
