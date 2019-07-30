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
//
//
// TODO:        FIGURE OUT WTF IS WRONG!

public class Server {

    private final static int PORT = 36979;

    private static int numClients;        // ID for each connection.
    private static ArrayList<ClientHandler> clients;   // List of clients.
    private static ServerSocket serverSocket;
//    private SimpleDateFormat simpleDateFormat;  // For displaying time.


    static ArrayList<ClientHandler> getActiveClients() {
        return clients;
    }


    static void removeClient(int ID) {

//        for (ClientHandler clientHandler : Server.getActiveClients()) {
        for (int i = 0; i < clients.size(); ++i) {
            ClientHandler clientHandler = clients.get(i);

            if (clientHandler.getID() == ID) {
                clients.remove(i);
                System.out.println("CLIENT " + clientHandler.getUsername() + 
                        " HAS" + " LOGGED OUT.");
            }
        }

    }

    /*
     * To run server, simply enter:
     *      java Server
     * into the command prompt.
     */
    public static void main(String[] args) {

        Thread thread;
        Socket socket;
        ClientHandler clientHandler;
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
            socket = serverSocket.accept();


            // NEW NEW
            //
            //
            //
            //



            // Make new data streams.
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            System.out.println("CREATING NEW HANDLER FOR CLIENT.");
            clientHandler = new ClientHandler(socket, numClients++, ois, oos);

            // Create new thread out of ClientHandler.
            thread = new Thread(clientHandler);

            // Add client to activeClients list.
            clients.add(clientHandler);
            
            // Start the new thread.
            thread.start();

            // END NEW NEW
            //
            //
            //
            //
            //
            //

            // Create ClientHandler for new Client, add to list, & start.
//            ClientHandler clientHandler = new ClientHandler(socket,
//                    numClients++);
//            clients.add(clientHandler);
//            clientHandler.start();



            } catch (IOException e) {
                System.err.println("ERROR:  COULD NOT GET DATA STREAMS!");
                e.printStackTrace();
            }
        }
   



    } /* END main() */
} /* END Server.java */
