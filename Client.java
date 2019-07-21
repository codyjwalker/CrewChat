/*
 * File:        Client.java
 * Purpose:     Client class that can either be run standalone through a 
 *              terminal or used by the GUI.
 */


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client {

    private final static int PORT = 36979;
    private final static String server = "crewchat.hopto.org";

    private ObjectInputStream ois;      // Read from socket.
    private ObjectOutputStream oos;     // Write to socket.
    private Socket socket;              // Connection to server.
    private ClientGUI clientGUI;        // NULL if launched from terminal.
    private String username;            // Name set by client user.
    

    // Constructor invoked by terminal-only launch.
    Client(String username) {
        this(username, null);
    }


    // Constructor invoked by GUI.
    Client(String username, ClientGUI clientGUI) {
        this.username = username;
        this.clientGUI = clientGUI;
    }


    /*
     * Starts everything up.
     */
    private boolean init() {
        String tmp;

        // Try connecting to Server.
        try {
            this.socket = new Socket(server, PORT);
        } catch (Exception e) {
            display("ERROR CONNECTING TO SERVER! " + e);
            // If connection failed, return false.
            return false;
        }
        tmp = "CONNECTION ESTABLISHED WITH SERVER: " +
            this.socket.getInetAddress() + ":" + this.socket.getPort();
        display(tmp);

        // Initialize Data Streams.
        try {
            this.ois = new ObjectInputStream(this.socket.getInputStream());
            this.oos = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (Exception e) {
            display("ERROR CREATING OBJECT INPUT/OUTPUT STREAMS! " + e);
            // If streams fail to initialize, return false.
            return false;
        }

        // Start a thread for listening to the server.
        new ServerListener().start();

        // Send username to server as String (unlike ALL other messages!)
        try {
            oos.writeObject(username);
        } catch (Exception e) {
            display("ERROR:  COULD NOT SEND USERNAME TO SERVER: ");
            e.printStackTrace();
            disconnect();
            return false;
        }

        // If we've made it this far, SUCCESS!!!
        return true;
    }


    /*
     * Closes I/O streams and socket, & informs GUI about shutdown if
     * GUI is running.
     */
    private void disconnect() {
        try {
            if (this.ois != null) {
                this.ois.close();
            }
            if (this.oos != null) {
                this.oos.close();
            }
            if (this.socket != null) {
                this.socket.close();
            }
            if (this.clientGUI != null) {
                this.clientGUI.connectionFailed();
            }
        } catch (Exception e) {
            System.err.println("WELLLLLL THIS IS BAD");
        }
    }


    /*
     * Either display message in GUI if it's running, or simply print it
     * to System.out if not.
     */
    private void display(String message) {
        if (this.clientGUI != null) {
            this.clientGUI.append(message + "\n");
        } else {
            System.out.print(message);
        }
    }


    /*
     * Send a message to the server.
     */
    private void sendMessage(Message message) {
        try {
            oos.writeObject(message);
        } catch (Exception e) {
            display("ERROR:  COULD NOT SEND TO SERVER! " + e);
        }
    }


    /*
     * To start Client in "terminal mode" use either of the following:
     *
     *          java Client
     *      or
     *          java Client username
     *
     * If username not specified, "ANON" will be used as the username.
     */
    public static void main(String[] args) {
        String message, username;
        Scanner scanner;

        // Set default username.
        username = "ANON";

        // Take username from arguments if user entered their username.
        if (args.length == 1) {
            username = args[0];
        }

        // Create Client Object.
        Client client = new Client(username);

        // Try connecting to Server, return & exit upon failure.
        if (!client.init()) {
            return;
        }

        // Wait for user to type a message.
        scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");

            // Get message from user.
            message = scanner.nextLine();

            if (message.equalsIgnoreCase("LOGOUT")) {
                // Log out if message is LOGOUT.
                client.sendMessage(new Message(Message.LOGOUT, ""));
                // Disconnect from server by breaking out of while()
                break;
            } else if (message.equalsIgnoreCase("CREWAT")) {
                // Send request for who's in the chat server.
                client.sendMessage(new Message(Message.CREWAT, ""));
            } else {
                // Regular message by default.
                client.sendMessage(new Message(Message.MESSAGE, message));
            }
        }
        // Disconnect from server once we've broken out of infinite loop.
        client.disconnect();
        return;
    }



    /*
     * Class for listening to the server by waiting for the messages from it
     * and then appending them to the JTextArea if GUI is running, or
     * printing to System.out if not.
     */
    class ServerListener extends Thread {
    
        public void run() {
            while (true) {
                try {
                    String message = (String) ois.readObject();
                    // Print to System.out if GUI is not running.
                    if (clientGUI == null) {
                        System.out.println(message);
                        System.out.print("> ");
                    } else {
                        clientGUI.append(message);
                    }
                } catch (Exception e) {
                    display("ERROR:  SERVER HAS CLOSED THE CONNECTION! "
                            + e);
                    if (clientGUI != null) {
                        clientGUI.connectionFailed();
                    }
                    break;
                }
            }
        }
    }
}
