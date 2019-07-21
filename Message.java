
/*
 * File:        Message.java
 * Purpose:     Defines the different type of messages that can be exchanged
 *              between a Client and the Server, and represents the physical
 *              message object being sent around.
 */
public class Message implements Serializable {

    // Generated with Eclipse.... used to avoid InvalidClassException error.
    private static final long serialVersionUID = -8054809742223994483L;

    private int type;
    private String message;

    // Message Types:
    static final int MESSAGE = 0;   // Text message.
    static final int LOGOUT = 1;    // Disconnect from server.
    static final int CREWAT = 2;    // Get list of users currently connected.

    Message(int type, String message) {
        this.type = type;
        this.message = message;
    }

    int getType() {
        return this.type;
    }

    String getMessage() {
        return this.message;
    }

}
