/*
 * File:        Server.java
 * Purpose:     Acts as a Server for the CrewChat program.  Responsible for
 *              receiving messages from all the clients, and then sending
 *              them out to all the clients.
 */


import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import java.text.SimpleDateFormat;

public class Server {

    private final static int PORT = 36979;

    private static int numClients;        // ID for each connection.
    private static ArrayList<ClientHandler> clients;   // List of clients.
    private static ServerSocket serverSocket;
//    private SimpleDateFormat simpleDateFormat;  // For displaying time.


    /*
     * To run server, simply enter:
     *      java Server
     * into the command prompt.
     */
    public static void main(String[] args) {


        ObjectInputStream ois;
        ObjectOutputStream oos;


        // Start a new Server.
        numClients = 0;
        clients = new ArrayList<ClientHandler>();

        try {
            // BIND to port.
            serverSocket = new ServerSocket(PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Infinite loop where Server waits for & accepts new connections.
        while (true) {
            System.out.println("WAITING FOR CLIENTS ON PORT " + PORT + ".");

            try {
            // Accept connection from new client.
            Socket socket = serverSocket.accept();


            // NEW NEW

            // Make new data streams.
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());


            // END NEW NEW
            //
            //
            //
            //
            //
            //

            // Create ClientHandler for new Client, add to list, & start.
            ClientHandler clientHandler = new ClientHandler(socket,
                    numClients++);
            clients.add(clientHandler);
            clientHandler.start();
            } catch (IOException e) {
                System.err.println("ERROR:  COULD NOT GET DATA STREAMS!");
                e.printStackTrace();
            }
        }
   



    } /* END main() */
} /* END Server.java */
