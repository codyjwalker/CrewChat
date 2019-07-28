#!/bin/sh
rm CrewChat.jar
jar cmf Client.mf CrewChat.jar Client.class ClientGUI.class Client\$ServerListener.class Message.class
chmod 777 CrewChat.jar
cp CrewChat.jar CrewChat/
