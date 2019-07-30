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
import java.util.Date;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Server {

    private final static int PORT = 36979;

    private static int uniqueID;        // ID for each connection.

    private ArrayList<ClientHandler> clients;   // List of clients.
//    private SimpleDateFormat simpleDateFormat;  // For displaying time.

    
    private void start() {
        ServerSocket serverSocket;
        uniqueID = 0;
        clients = new ArrayList<ClientHandler>();

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
    } /* END start() */


    /*
     * Broadcast message to all clients in the Server.
     */
    private synchronized void broadcast(String message) {
        // Add time to beginning and newline to end of message.
        //String timeStr = this.simpleDateFormat.format(new Date());
        //String finalMessage = timeStr + " " + message + "\n";
        String finalMessage = message + "\n";
        System.out.print(finalMessage);

        // Loop in reverse order in case a Client disconnected.
        for (int i = clients.size(); --i >= 0;) {

            ClientHandler clientHandler = clients.get(i);

            // Try writing to Client & REMOVE client if writing fails.
            if (!clientHandler.writeMessage(finalMessage)) {
                clients.remove(i);
                System.out.println("DISCONNECTED CLIENT " +
                        clientHandler.username);
            }
        }
    } /* END broadcast() */


    /*
     * Called by ClientHandler of a Client upon logging out.
     */
    private synchronized void remove(int ID) {

        for (int i = 0; i < clients.size(); ++i) {
            ClientHandler clientHandler = clients.get(i);
            if (clientHandler.ID == ID) {
                clients.remove(i);
                System.out.println("CLIENT " + clientHandler.username + " HAS"
                        + " LOGGED OUT.");
            }
        }
    } /* END remove() */


    /*
     * To run server, simply enter:
     *      java Server
     * into the command prompt.
     */
    public static void main(String[] args) {
        // Start a new Server.
        new Server().start();
        return;
    } /* END main() */
} /* END Server.java */
