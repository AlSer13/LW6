package Communication;

import Graphics.GameField;
import Graphics.Login;
import Graphics.Unit;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


import java.io.*;
import java.net.*;
import java.util.*;


public class AlternativeClient extends Application {

    public Stack<Unit> units = new Stack<>();

    private int localPort = 0;

    public ObjectOutputStream oos;
    public ObjectInputStream ois;

    private int readPort() {
        int port = 0;
        //Scanner scan = new Scanner(System.in);
        do {
            System.out.println("Enter port:");
            try {
                //localPort = Integer.parseInt(scan.next());
                port = 3345;
                System.out.println(port);
                if (port > 65000) {
                    port = 0;
                    throw new NumberFormatException("Port out of range");
                }
            } catch (NumberFormatException e) {
                System.out.println("Should be port number");
            }
        } while (port == 0);
        return port;
    }

    private int i; //Connection attempts

    public void start(Stage primaryStage) {

        localPort = readPort();
        Login login = new Login(this, primaryStage);

    }

    public void connectLocal() {

        try {

            Socket socket = new Socket("localhost", localPort);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            System.out.println((String) ois.readObject());

            //пишется из первой строки цикла в MultiClientThread
            units = (Stack<Unit>) ois.readObject();
            oos.reset();
            oos.writeObject(null);
            oos.flush();


        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

            try {

                new Alert(Alert.AlertType.ERROR, (i < 2 ? "\nFailed to connect to server (╯°□°）╯︵ ┻━┻`" : "\n┻━┻ ︵ヽ(`Д´)ﾉ︵﻿ ┻━┻")).showAndWait();
                Thread.sleep(5000);
                System.out.println("\nSending request again");
                if (i == 10) System.exit(-1);
                i++;
                connectLocal();

            } catch (InterruptedException e1) {

                e1.getMessage();

            }

        }
    }
}











/*-********************OLD CODE***************************/


   /* public void receiveMsgs() {
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

            //BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

            while (sending) {
                try {
                    if (units != null)
                        oos.writeObject(units);
                    //----------------------------------------------------------------------------
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
                    //-----------------------------------------------------------------------------------------------------

                } catch (IOException e) {
                    System.out.println("Connection aborted");
                    System.exit(-1);
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
        public ReentrantLock initLock = new ReentrantLock();

        public boolean listening = true;

        @Override
        public void run() {

            try {
                initLock.lock();
                System.out.println(ois.readObject());
                units = (Stack<CarouselItem>) ois.readObject();
                initLock.unlock();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initLock.lock();
                oos.writeObject(units);
                initLock.unlock();
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initLock.lock();



                do {


                    System.out.println(msg);
                    switch (msg) {
                        case "generating":
                            Stack<CarouselItem> events = (Stack<Event>) ois.readObject();
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
                    listening = false;

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
    }*/

