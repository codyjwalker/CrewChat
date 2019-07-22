/*
 * File:        ClientGUI.java
 * Purpose:     GUI window for the Client of the CrewChat program.
 */


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;


public class ClientGUI extends JFrame implements ActionListener {

    private static final String HOST = "crewchat.hopto.org";
    private static final int PORT = 36979;
    private static final long serialVersionUID = -8054809742223994483L;

    private boolean connected;
    private JLabel label;
    private JTextField textField;
    private JButton loginButton, logoutButton, crewAtButton;
    private JTextArea textArea;
    private Client client;

    public ClientGUI() {
        super("Chat Client");

        // 3 grids tall, 1 grid wide.
        JPanel northPanel = new JPanel(new GridLayout(3, 1));

        // Label.
        label = new JLabel("ENTER USERNAME BELOW:", SwingConstants.CENTER);
        northPanel.add(label);

        // TextField.
        textField = new JTextField("ANON");
        textField.setBackground(Color.WHITE);
        northPanel.add(textField);
        add(northPanel, BorderLayout.NORTH);

        // Center panel for the chatroom.
        textArea = new JTextArea("WELCOME TO THE CREW =D\n", 80, 80);
        JPanel centerPanel = new JPanel(new GridLayout(1, 1));
        centerPanel.add(new JScrollPane(textArea));
        textArea.setEditable(false);
        add(centerPanel, BorderLayout.CENTER);

        // The three buttons.
        loginButton = new JButton("LOG IN");
        loginButton.addActionListener(this);
        logoutButton = new JButton("LOG OUT");
        logoutButton.addActionListener(this);
        logoutButton.setEnabled(false);     // Cant logout until logged in!
        crewAtButton = new JButton("MANNN, WHERE MY CREW AT?!?");
        crewAtButton.setEnabled(false);     // Not until logged in silly!
        JPanel southPanel = new JPanel();
        southPanel.add(loginButton);
        southPanel.add(logoutButton);
        southPanel.add(crewAtButton);
        add(southPanel, BorderLayout.SOUTH);

        // Characteristics of Window.
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
        textField.requestFocus();




    }


    public static void main(String[] args) {
        new ClientGUI();
    }


    protected void connectionFailed() {
    }


    protected void append(String message) {
    }


    public void actionPerformed(ActionEvent e) {
    }
}
