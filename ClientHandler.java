/*
 * Each Client will have their own ClientHandler running as its own
 * thread.
 */


import java.net.Socket;
import java.util.Date;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//
//
//
//
//
//
//
// TODO:      FIGURE OUT WTF IS WRONG!

class ClientHandler extends Thread {

    private int ID;                 // Client's unique ID.
    private String username;        // The client's username.
    private Socket cliSock;         // Socket to the Client.
    private ObjectInputStream ois;  // Read from socket.
    private ObjectOutputStream oos; // Write to socket.
    private Message message;        // The message being passed.
    private String date;            // The date/time for message.





    /*
     * Constructor for ClientHandler, takes socket from Server.
     */
    ClientHandler(Socket socket, int ID, ObjectInputStream ois,
            ObjectOutputStream oos) {
        // Assign a new unique ID to new Client.
        this.ID = ID;
        this.cliSock = socket;
        this.ois = ois;
        this.oos = oos;

        try {
            // Get username.
            this.username = (String) ois.readObject();
            System.out.println(username + " HAS JOINED DA CREW!");
        } catch (Exception e) {
            System.err.println("ERROR!  COULD NOT READ USERNAME!");
            e.printStackTrace();
            return;
        }

        date = new Date().toString() + "\n";
    } /* END CliendHandler() */


    int getID() {
        return this.ID;
    }

    String getUsername() {
        return this.username;
    }



    /*
     * Needed since ClientHandler extends Thread.... contains the
     * infinite loop and runs until the user logs out.
     */
    public void run() {
        String textMsg;
        boolean keepAlive = true;

        while (keepAlive) {
            try {
                // Read Message Object.
                this.message = (Message) ois.readObject();
                
                // TODO: INVESTIGATE IF THIS IS CAUSING ERRORS TO BE THROWN
                //       ON LOGOUT (OR PERHAPS ALSO CREWAT?)
                // Extract textual part of Message
                textMsg = message.getMessage();

                switch (message.getType()) {

                    // Regular message, broadcast it.
                    case Message.MESSAGE:
                        broadcast(username + ": " + textMsg);
                        break;

                    // Logout message, log the user out.
                    case Message.LOGOUT:
                        System.out.println(username + " HAS LOGGED OUT.");
                        keepAlive = false;
                        break;

                    // MAN WHERE MY CREW AT?!?!?!?!?
                    case Message.CREWAT:
                        writeMessage("THE CREW IS AS FOLLOWS" +
//                                    simpleDateFormat.format(new Date()) +
                                ":\n");
                        // Scan ArrayList to get all da Crew.
                        int i = 0;
                        for (ClientHandler clientHandler :
                                Server.getActiveClients()) {
//                        for (int i = 0; i < clients.size(); i++) {
//                            ClientHandler clientHandler =
//                                clients.get(i);
                            writeMessage((i + 1) + ".) " +
                                    clientHandler.username
                                    + " SINCE " + clientHandler.date);
                            i++;
                        }
                        break;
                } /* END switch() */
            } catch (Exception e) {
                System.err.println("ERROR:  COULD NOT READ MESSAGE!");
                e.printStackTrace();
                break;
            }
        } /* END while() */
        // If out of infinite loop, remove self from Clients list.
        remove(ID);
        close();
    } /* END run() */


    /*
     * Cleanup!!!
     */
    private void close() {
        try {
            if (this.ois != null) {
                this.ois.close();
            }
            if (this.oos != null) {
                this.oos.close();
            }
            if (this.cliSock != null) {
                this.cliSock.close();
            }
        } catch (Exception e) {
        }
    } /* END close() */


    /*
     * Write String to ClientHandler's output stream to be read in by
     * Client's input stream.
     */
    private boolean writeMessage(String message) {
        if (!cliSock.isConnected()) {
            close();
            return false;
        }

        try {
            oos.writeObject(message);
        } catch (Exception e) {
            System.err.println("ERROR SENDING MESSAGE TO " + username);
            e.printStackTrace();
        }

        return true;
    } /* END writeMessage() */

























    /*
     * Called by ClientHandler of a Client upon logging out.
     */
    private synchronized void remove(int ID) {

        Server.removeClient(ID);

        /*
//        for (ClientHandler clientHandler : Server.getActiveClients()) {
        for (int i = 0; i < clients.size(); ++i) {
            ClientHandler clientHandler = clients.get(i);
            if (clientHandler.ID == ID) {
                clients.remove(i);
                System.out.println("CLIENT " + clientHandler.username + " HAS"
                        + " LOGGED OUT.");
            }
        }
        */
    } /* END remove() */


    /*
     * Broadcast message to all clients in the Server.
     */
    private synchronized void broadcast(String message) {
        // Add time to beginning and newline to end of message.
        //String timeStr = this.simpleDateFormat.format(new Date());
        //String finalMessage = timeStr + " " + message + "\n";
        String finalMessage = message + "\n";
        System.out.print(finalMessage);


        int i = 0;

        for (ClientHandler clientHandler : Server.getActiveClients()) {
        // Loop in reverse order in case a Client disconnected.
//        for (int i = clients.size(); --i >= 0;) {

//            ClientHandler clientHandler = clients.get(i);

            // Try writing to Client & REMOVE client if writing fails.
            if (!clientHandler.writeMessage(finalMessage)) {
//                clients.remove(i);
                remove(clientHandler.getID());
                System.out.println("DISCONNECTED CLIENT " +
                        clientHandler.username);
            }
        }
    } /* END broadcast() */









} /* END ClientHandler.java */
