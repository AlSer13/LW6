package Communication;

import CollectionCLI.CollectionHandler;
import CollectionCLI.Instruments;
import Plot.Event;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


import java.io.*;
import java.net.*;
import java.util.*;

import static CollectionCLI.Instruments.*;

public class AlternativeClient {

/**
    String username = "s242463";
    String host = "helios.cs.ifmo.ru";
    int port = 2222;

    String pswd = "";
*/

    int localPort = 0;

    public Receiving rt;
    public Sending st;

    ObjectOutputStream oos;
    ObjectInputStream ois;

    int i; //Connection attempts
    public static void main(String[] args) throws InterruptedException {
        AlternativeClient tc = new AlternativeClient();

        Scanner scan = new Scanner(System.in);
        do {
            System.out.println("Enter port:");
            try {
                tc.localPort = Integer.parseInt(scan.next());
                if (tc.localPort > 65000) {
                    tc.localPort = 0;
                    throw new NumberFormatException("Port out of range");
                }
            } catch (NumberFormatException e) {
                System.out.println("Should be port number");
            }
        } while (tc.localPort == 0);
        tc.connectLocal();
        tc.receiveMsgs();
        tc.sendMsgs();
    }


    /**public boolean connect() {

        try {

            JSch jSch = new JSch();

            Session session = jSch.getSession(username, host, port);
            System.out.println("Enter pswd:");
            Scanner scan = new Scanner(System.in);
            pswd = scan.nextLine();
            session.setPassword(pswd);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications",
                    "publickey,keyboard-interactive,password");

            session.connect();
            Channel channel = session.getStreamForwarder(host, localPort);
            channel.connect();

            oos = new ObjectOutputStream(channel.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(channel.getInputStream());

            return true;

        } catch (JSchException e) {
            try {
                System.out.print(i < 2 ? "\nFailed to connect to server " : "\n┻━┻ ︵ヽ(`Д´)ﾉ︵﻿ ┻━┻");
                Thread.sleep(3000);
                System.out.println("\nSending request again");
                if (i == 10) System.exit(-1);
                i++;

                connect();

            } catch (InterruptedException e1) {
                e1.getMessage();
            }

            return false;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }*/

    public boolean connectLocal() {

        try {

            Socket socket = new Socket("localhost", localPort);

            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());
            return true;

        } catch (ConnectException e) {

            try {

                System.out.print(i < 2 ? "\nFailed to connect to server (╯°□°）╯︵ ┻━┻`" : "\n┻━┻ ︵ヽ(`Д´)ﾉ︵﻿ ┻━┻");
                Thread.sleep(3000);
                System.out.println("\nSending request again");
                if (i == 10) System.exit(-1);
                i++;
                connectLocal();

            } catch (InterruptedException e1) {

                e1.getMessage();

            }
            return false;

        } catch (IOException e) {
            return false;
        }

    }


    public void receiveMsgs() {
        rt = new Receiving();
        rt.start();
    }


    public void sendMsgs() {
        st = new Sending();
        st.start();
    }


    public class Sending extends Thread {

        public boolean sending = true;

        @Override
        public void run() {

            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

            while (sending) {
                try {

                    ArrayList<String> cmd = parseCmd(multilineJson(consoleIn));

                    if (cmd.get(0).equals("help")) {
                        String help = "•\tremove_last: удалить последний элемент из коллекции\n" +
                                "•\timport {String path}: добавить в коллекцию все данные из файла\n" +
                                "•\tremove_all {element}: удалить из коллекции все элементы, эквивалентные заданному\n" +
                                "•\treorder: отсортировать коллекцию в порядке, обратном нынешнему\n" +
                                "•\tsave: сохранить коллекцию в файл\n" +
                                "•\tremove {int index}: удалить элемент, находящийся в заданной позиции коллекции\n" +
                                "•\tinfo: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                                "•\tremove {element}: удалить элемент из коллекции по его значению\n" +
                                "•\tadd_if_max {element}: добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                                "•\tremove_greater {element}: удалить из коллекции все элементы, превышающие заданный\n" +
                                "•\tinsert {int index} {element}: добавить новый элемент в заданную позицию\n" +
                                "•\tadd_if_min {element}: добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
                                "•\tremove_first: удалить первый элемент из коллекции\n" +
                                "•\tremove_lower {element}: удалить из коллекции все элементы, меньшие, чем заданный\n" +
                                "•\tclear: очистить коллекцию\n" +
                                "•\tadd {element}: добавить новый элемент в коллекцию\n" +
                                "•\tload: перечитать коллекцию из файла\n" +
                                "•\tcontents: содержание коллекции\n" +
                                "•\tquit: закончить сеанс клиента";
                        System.out.println(help);
                        cmd.set(0, "null");
                    }

                    if (CollectionHandler.objComms.contains(cmd.get(0))) {
                        Event event = null;
                        try {
                            event = cmd.get(0).equals("insert") ? fromJson(cmd.get(2)) : fromJson(cmd.get(1));
                        } catch (com.google.gson.JsonSyntaxException e) {
                            System.out.println("Wrong Json format");
                            cmd.set(0, "null");
                        }
                        oos.writeObject(cmd);
                        oos.flush();
                        //if (event != null) {
                            oos.writeObject(event);
                            oos.flush();
                        //}
                    } else {
                        oos.writeObject(cmd);
                        oos.flush();
                    }

                } catch (IOException e) {
                    System.out.println("Connection aborted");
                    System.exit(-1);
                } catch (Instruments.WrongArgsException e) {
                    System.out.println(e.getMessage());
                } catch (NullPointerException e) {
                    System.out.println("Output closed");
                } catch (StringIndexOutOfBoundsException | NoSuchElementException e) {
                    System.out.println("Wrong command format");
                }
            }

            System.out.println("Finished sending");

        }
    }


    public class Receiving extends Thread {

        public boolean listening = true;

        @Override
        public void run() {

            try {

                do {

                    String msg = (String) ois.readObject();
                    System.out.println(msg);
                    switch (msg) {
                        case "generating":
                            Stack<Event> events = (Stack<Event>) ois.readObject();
                            events.forEach(e -> {
                                System.out.println("\n" + e.name + ":");
                                e.go();
                            });
                            System.out.println("Generated");
                            break;

                        case "Quitting...":
                            listening = false;
                            break;

                    }

                } while (listening);

                System.out.println("Connection closed");
                ois.close();
                oos.close();
                System.exit(0);

            } catch (IOException e) {
                System.out.println("ಥ﹏ಥ");

            } catch (NullPointerException e) {
                System.out.println("Non-existent channel. Try again.");
                System.exit(-1);

            } catch (ClassNotFoundException e) {
                System.out.println("Class not found");
            }
        }
    }

}