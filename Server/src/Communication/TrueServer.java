package Communication;

import Communication.Protocol;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TrueServer {

    public static void main(String[] args) throws InterruptedException {
        try (
                ServerSocket serverSocket = new ServerSocket(3345);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {
            Protocol pc = new Protocol();
            String inputLine, outputLine;
            out.println("Connected: " + clientSocket.getInetAddress().getHostAddress() + ": " + clientSocket.getPort());
            while ((inputLine = in.readLine())!=null) {
                System.out.println("Message recieved: " + inputLine);
                outputLine = pc.proceedResponse(inputLine);
                out.println(outputLine);
                if (inputLine.equals("quit")){
                    break;
                }
            }
            System.out.println("Disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
