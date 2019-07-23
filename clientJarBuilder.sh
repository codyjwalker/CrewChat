#!/bin/sh
rm Client.jar
jar cmf Client.mf Client.jar Client.class ClientGUI.class ClientGUI.java Client.java Client\$ServerListener.class Message.class Message.java
