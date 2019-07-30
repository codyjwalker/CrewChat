/*
 * File:        ChatRoom.java
 * Purpose:     Represents a chat room as a part of the Server.  That way,
 *              Clients can choose separate chat rooms to communicate
 *              with only those in that chatroom, rather than everybody
 *              being forced to share the same space for different
 *              conversations.
 */


import java.util.ArrayList;

public class ChatRoom {


    private String name;
    private ArrayList<ClientHandler> chatRoomClients;


    public ChatRoom(String name) {
        this.name = name;
        this.chatRoomClients = new ArrayList<ClientHandler>();
    }

    
    private void addChatRoomClient(ClientHandler clientHandler) {
        this.chatRoomClients.add(clientHandler);
    }


    private ArrayList<ClientHandler> getChatRoomClients() {
        return this.chatRoomClients;
    }


} /* END ChatRoom.java */
