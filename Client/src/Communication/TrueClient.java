package Communication;

import CollectionCLI.Instruments;
import Plot.Event;
import Plot.WTPcharacter;


import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static CollectionCLI.Instruments.multilineJson;
import static CollectionCLI.Instruments.parseCmd;

public class TrueClient {
    public SocketChannel channel;
    public InetSocketAddress address;
    public Receiving rt;
    public Sending st;
    static Lock dotlock = new ReentrantLock();
    static Lock initlock = new ReentrantLock();
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

    public static void main(String[] args) throws InterruptedException {
        // dots.start();
        TrueClient tc = new TrueClient();
        tc.connect();
        // dotlock.lock();
        tc.receiveMsgs();
        tc.sendMsgs();
    }

    static Thread dots = new Thread(() -> {
        while (true) {
            dotlock.lock();
            System.out.print(".");
            dotlock.unlock();
            try {
                Thread.sleep(450);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }); //Бегущие точечки
    int i = 0;
    public void connect() {
        try {
            address = new InetSocketAddress("localhost", 3345);
            channel = SocketChannel.open(address);
        } catch (ConnectException e) {
            try {
                // dotlock.lock();
                System.out.print(i<2 ? "\nFailed to connect to server (╯°□°）╯︵ ┻━┻`" : "\n┻━┻ ︵ヽ(`Д´)ﾉ︵﻿ ┻━┻");
                i++;
                Thread.sleep(3000);
                System.out.println("\nSending request again");
                // dotlock.unlock();
                if (i == 20) System.exit(-1);
                connect();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Sending extends Thread {
        public SocketChannel channel;
        public boolean sending = true;

        public Sending(String name, SocketChannel channel) {
            super(name);
            this.channel = channel;
        }

        @Override
        public void run() {
            int capacity = 1024;
            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
            ByteArrayOutputStream baos = new ByteArrayOutputStream(capacity);
            ObjectOutputStream oos = null;
            ByteBuffer buffer = ByteBuffer.allocate(capacity);
            try {
                oos = new ObjectOutputStream(baos);
                oos.flush();
                buffer.put(baos.toByteArray());
                buffer.flip();
                channel.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String msg;
            while (sending) {
                try {
                    buffer.clear();
                    baos.reset();
                    ArrayList<String> cmd = parseCmd(multilineJson(consoleIn));
                    if (cmd.get(0).equals("help")) {
                        System.out.println(help);
                        cmd.set(0, "null");
                    }
                    oos.writeObject(cmd);
                    oos.flush();
                    buffer.put(baos.toByteArray());
                    buffer.flip();
                    channel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Instruments.WrongArgsException e) {
                    System.out.println(e.getMessage());
                } catch (NullPointerException e) {
                    System.out.println("Failed to create ObjectOutputStream");
                } catch (StringIndexOutOfBoundsException | NoSuchElementException e) {
                    System.out.println("Wrong command format");
                }
            }
            System.out.println("Finished sending");
            try {
                channel.shutdownOutput();
            } catch (IOException e) {
                System.out.println("Failed to shutdown output");
            }
        }
    }

    public void receiveMsgs() {
        rt = new Receiving("Receive Thread", channel);
        rt.start();
    }

    public void sendMsgs() {
        st = new Sending("Send Thread", channel);
        st.start();
    }

    public class Receiving extends Thread {
        public SocketChannel channel;
        public boolean listening = true;


        public Receiving(String name, SocketChannel channel) {
            super(name);
            this.channel = channel;
        }

        private String rcvMsg(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            String msg = (String) ois.readObject();
            return msg;
        }

        @Override
        public void run() {
            int buffCapacity = 4000;
            try {
                ByteBuffer buffer = ByteBuffer.allocate(buffCapacity);
                byte data[] = buffer.array();
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois;
                channel.configureBlocking(true);
                do {
                    channel.read(buffer);
                } while (buffer.get(7) == 0);
                ois = new ObjectInputStream(bais);
                do {
                    String msg = rcvMsg(ois);
                    System.out.println(msg);
                    bais.reset();
                    do {
                        buffer.clear();
                        channel.read(buffer);
                    } while (buffer.position() == 0 && listening);
                    switch (msg) {
                        case "generating":
                            ArrayList<String> w = (ArrayList<String>) ois.readObject();
                            System.out.println("hi");
                            //events.forEach(e -> System.out.println(e.name));
                           // System.out.println("Generated");
                            bais.reset();
                            do {
                                buffer.clear();
                                channel.read(buffer);
                            } while (buffer.position() == 0 && listening);
                            break;
                        case "Quitting...":
                            listening = false;
                            break;
                    }
                } while (listening);
                System.out.println("Finished receiving");
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("ಥ﹏ಥ");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("Non-existent channel");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}