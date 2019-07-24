#!/bin/sh
rm CrewChat.jar
jar cmf Client.mf CrewChat.jar Client.class ClientGUI.class ClientGUI.java Client.java Client\$ServerListener.class Message.class Message.java
chmod 777 CrewChat.jar
