package Communication;

import CollectionCLI.CollectionHandler;
import Graphics.Unit;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Stack;

public class TrueServer {
    public static void main(String[] args) throws IOException {
        Stack<Unit> units = new Stack<>();
        int port = 0;
        CollectionHandler ch = new CollectionHandler();
        ShutdownHook shutdownHook = new ShutdownHook(ch);
        Runtime.getRuntime().addShutdownHook(shutdownHook);


        //server
        boolean listening = true;
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
        } while (port==0);
        try (
                ServerSocket serverSocket = new ServerSocket(port)
        ) {
            while (listening) {
                new MultiClientThread(serverSocket.accept(), /*ch,*/ units).start();
            }
            System.out.println("Disconnected");
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
        }
    }
}
class ShutdownHook extends Thread {
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
}









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

