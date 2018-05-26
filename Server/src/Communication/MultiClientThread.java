package Communication;

import CollectionCLI.CollectionHandler;
import Graphics.Unit;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class MultiClientThread extends Thread {

    //private CollectionHandler ch;

    private Socket socket;
    private Stack<Unit> units;


    MultiClientThread(Socket socket, /*CollectionHandler ch,*/ Stack<Unit> units) {

        super("MultiClientThread");
        this.socket = socket;
        //this.ch = ch;
        this.units = units;

    }


    public void run() {

        try {

            boolean listening = true;

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject("Connected: " + socket.getInetAddress().getHostAddress() +
                    ":" + socket.getLocalPort() +
                    "." + socket.getPort()); //читаается в connectLocal()
            System.out.println("Client connected " + socket.getPort());

            Unit unit;
            do {

                try {

                    //в первый раз прочиталось в connectLocal()
                    out.reset();
                    out.writeObject(units);
                    out.flush();
                    unit = (Unit) in.readObject();
                    if (unit!=null)
                    if (units.contains(unit)) {
                        units.set(units.indexOf(unit), unit);
                    } else units.add(unit);

                } catch (SocketException e) {
                    System.out.println("Somebody killed the connection. Who in the world it might be? \uD83E\uDD14" +
                            Emoji.thinking);
                    listening = false;

                } catch (NullPointerException e) {
                    System.out.println("NPE thrown");

                } catch (IOException | ClassNotFoundException e) {
                    //do nothing
                }

            } while (listening);


        } catch (IOException e) {
            System.out.println("Connection killed violently");
        }


        System.out.println(socket.getPort() + " disconnected.");
        try {
            socket.close();
        } catch (IOException e) {
            //do nothing
        }
    }
}
