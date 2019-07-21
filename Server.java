/*
 * File:        Server.java
 * Purpose:     Acts as a Server for the CrewChat program.  Responsible for
 *              receiving messages from all the clients, and then sending
 *              them out to all the clients.
 */


import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final static int PORT = 36979;

    private static int uniqueID;        // ID for each connection.

    private ArrayList<ClientHandler> clients;   // List of clients.
    private SimpleDateFormat simpleDateFormat;  // For displaying time.

    
    private void start() {
        ServerSocket serverSocket;

        try {
            // Create ServerSocket.
            serverSocket = new ServerSocket(PORT);

            // Infinite loop where Server waits for & accepts new connections.
            while (true) {
                System.out.println("WAITING FOR CLIENTS ON PORT " + PORT + ".");

                // Accept connection from new client.
                Socket socket = serverSocket.accept();

                // Create ClientHandler for new Client, add to list, & start.
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }


    /*
     * To run server, simply enter:
     *      java Server
     * into the command prompt.
     */
    public static void main(String[] args) {
        // Start a new Server.
        new Server().start();
        return;
    }


    /*
     * Each Client will have their own ClientHandler running as its own
     * thread.
     */
    class ClientHandler extends Thread {

        ClientHandler(Socket socket) {
        }

        public void run() {
        }
    
    }

}
