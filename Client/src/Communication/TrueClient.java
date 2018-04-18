package Communication;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class TrueClient {

    public static void main(String[] args) throws InterruptedException {

        try (
                Socket clientSocket = new Socket("localhost", 3345);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                BufferedReader stdIn =
                        new BufferedReader(
                        new InputStreamReader(System.in))
        ) {
            String fromServer, fromUser;
            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("quit"))
                    break;

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    out.println(fromUser);
                }
            }
            System.out.println("Disconnected");
        } catch (IOException e) {
            System.out.println("The server is currently unavailable.\nPlease try again later.");

        }
    }
}