package Communication;

import GameFieldItems.Unit;
import Graphics.ServerRoom;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Stack;

public class TrueServer extends Application {

    private volatile ObservableList<Unit> unitsOL = FXCollections.observableArrayList();
    private Stack<Unit> units = new Stack<>();
    private HashMap<String, MultiClientThread> clientMap = new HashMap<>();
    private boolean paused;

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        int port = 0;
        File file = new File("SaveFile");

        //wait for port
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println("Enter port:");
            try {
                //port = scan.nextInt();
                port = 3345;
                System.out.println(port);
            } catch (InputMismatchException e) {
                System.out.println("Should be integer");
                scan.next();
            }
        } while (port == 0);

        int ports[] = {port};


        //receive Clients

        Service<Boolean> receiveClients = new Service<Boolean>() {

            @Override
            protected Task<Boolean> createTask() {
                return new Task<Boolean>() {
                    boolean listening = true;

                    @Override
                    protected Boolean call() {
                        try (ServerSocket serverSocket = new ServerSocket(ports[0])) {
                            while (listening) {
                                try {
                                    new MultiClientThread(serverSocket.accept(), /*ch,*/ units, unitsOL, clientMap, paused).start();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            System.err.println("Could not listen on port " + ports[0]);
                        }
                        System.out.println("Disconnected");
                        return true;
                    }
                };
            }
        };

        receiveClients.start();
        new ServerRoom(primaryStage, unitsOL, file, clientMap);


    }
}

/*class ShutdownHook extends Thread {
    private CollectionHandler ch;

    ShutdownHook(CollectionHandler inch) {
        this.ch = inch;
    }

    public void run() {
        try {
            ch.save();
        } catch (NullPointerException e) {
            System.out.println("No file");
        }
    }
}*/









/*-********************OLD CODE***********************/
//read from file
/*try {
            ch.file = new File(extractFilePath(args[0]), extractFileName(args[0]));
        } catch (NullPointerException e) {
            System.out.println("You should enter the path");
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Not enough arguments");
        }
        try {
            ch.load();
        } catch (SecurityException e) {
            System.out.println("Permission to the file Denied");
        } catch (IOException | NullPointerException e) {
            System.out.println("Wrong input file. Enter the command again.");
            System.exit(1);
        }*/

