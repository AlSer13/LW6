package Communication;


import GameFieldItems.Unit;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Stack;

public class MultiClientThread extends Thread {

    private Socket socket;
    private Stack<Unit> units = new Stack<>();
    private volatile ObservableList<Unit> unitsOL;
    private String cmd;
    private int arg;
    private boolean paused;
    private HashMap<String, MultiClientThread> clientMap;


    MultiClientThread(Socket socket, /*CollectionHandler ch,*/ Stack<Unit> units, ObservableList<Unit> unitsOL, HashMap<String, MultiClientThread> clientMap, boolean paused) {

        super("MultiClientThread");
        this.socket = socket;
        //this.ch = ch;
        this.units = units;
        this.unitsOL = unitsOL;
        this.clientMap = clientMap;
        this.paused = paused;

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
            /*WriteThread wt = new WriteThread(out);
            wt.start();*/

            Unit unit = null;

            do {

                try {


                    //в первый раз прочиталось в connectLocal()
                    //units.forEach(u -> u.setRemoved(unitsOL.get(unitsOL.indexOf(u)).isRemoved()));
                    out.reset();
                    out.writeObject(cmd);
                    out.flush();
                    if (cmd!=null)
                    switch (cmd){
                        case "kick": {
                            units.remove(unit);
                            unitsOL.remove(unit);
                            listening = false;
                            unit = null;
                            cmd = null;
                            break;
                        }
                        case "move": {
                            units.get(units.indexOf(unit)).setLocId(arg);
                            cmd = null;
                            break;
                        }
                        case "pause": {
                            this.paused = true;
                            cmd = null;
                            break;
                        }
                        case "play": {
                            this.paused = false;
                            cmd = null;
                            break;

                        }
                        case "load": {
                            if(unitsOL.contains(unit)) {
                                units.set(units.indexOf(unit), unitsOL.get(unitsOL.indexOf(unit)));
                                cmd = null;
                            } else
                            {
                                sendCmd("kick");
                            }
                            unitsOL.stream().filter(u -> !units.contains(u)).forEach(u -> units.add(u));
                            break;
                        }
                    }
                        out.reset();
                        out.writeObject(units);
                        out.flush();
                        unit = (Unit) in.readObject();

                    if (unit != null) {
                        if (units.contains(unit) && !unit.isKicked() && unitsOL.contains(unit)) {
                            units.set(units.indexOf(unit), unit);
                            unitsOL.get(unitsOL.indexOf(unit)).setLocId(unit.getLocId());
                            unitsOL.get(unitsOL.indexOf(unit)).setCharId(unit.getCharId());
                            unitsOL.get(unitsOL.indexOf(unit)).setRemoved(unit.isRemoved());
                            unitsOL.get(unitsOL.indexOf(unit)).setLocName(unit.getLocName());
                            unitsOL.get(unitsOL.indexOf(unit)).setCharName(unit.getCharName());
                        } else {
                            units.add(unit);
                            unitsOL.add(unit);
                            clientMap.putIfAbsent(unit.getName(), this);
                        }
                    }


                } catch (SocketException e) {
                    System.out.println("Somebody has killed the connection. Who in the world it could have been? \uD83E\uDD14");
                    listening = false;

                } catch (NullPointerException e) {
                    e.printStackTrace();

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

    public void sendCmd(String cmd) {
        this.cmd = cmd;
    }

    public void sendCmd(String cmd, int arg) {
        sendCmd(cmd);
        this.arg = arg;
    }
    //TODO Сделать раздельное получение и отправку

    /*public class WriteThread extends Thread {
        ObjectOutputStream out;

        WriteThread(ObjectOutputStream out) {
            this.out = out;
        }

        @Override
        public void run() {
            try {
                out.reset();
                out.writeObject(units);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}
