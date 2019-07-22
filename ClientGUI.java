/*
 * File:        ClientGUI.java
 * Purpose:     GUI window for the Client of the CrewChat program.
 */


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

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
//    private static final long serialVersionUID = -8054809742223994483L;

    private static final long serialVersionUID = -8054809829223994483L;

    private boolean connected;
    private JLabel label;
    private JTextField textField;
    private JButton loginButton, logoutButton, crewAtButton;
    private JTextArea textArea;
    private Client client;

    public ClientGUI() {
        super("Chat Client");

        // Input Panel: 3 grids wide, 1 grid tall.
        JPanel inputPanel = new JPanel(new GridLayout(3, 1));
        // Label.
        label = new JLabel("ENTER USERNAME BELOW:", SwingConstants.CENTER);
        inputPanel.add(label);
        // TextField.
        textField = new JTextField("ANON");
        textField.setBackground(Color.WHITE);
        inputPanel.add(textField);
        add(inputPanel, BorderLayout.NORTH);

        // Center panel for the chatroom.
        // TODO: JTextPane!!!
        textArea = new JTextArea("WELCOME TO THE CREW =D\n", 80, 30);
        JPanel chatPanel = new JPanel(new GridLayout(1, 1));
        chatPanel.add(new JScrollPane(textArea));
        textArea.setEditable(false);
        textArea.setBackground(new Color(28, 128, 54));
        add(chatPanel, BorderLayout.CENTER);

        // The three buttons.
        loginButton = new JButton("LOG IN");
        loginButton.addActionListener(this);
        logoutButton = new JButton("LOG OUT");
        logoutButton.addActionListener(this);
        logoutButton.setEnabled(false);     // Cant logout until logged in!
        crewAtButton = new JButton("MANNN, WHERE MY CREW AT?!?");
        crewAtButton.setEnabled(false);     // Not until logged in ya goof!
        crewAtButton.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(crewAtButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Characteristics of Window.
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 800);  // Height, Width
        setVisible(true);
        textField.requestFocus();
    }


    /*
     * Called by GUI upon failing to connect... reset buttons, label, &
     * textField.
     */
    void connectionFailed() {
        loginButton.setEnabled(true);
        logoutButton.setEnabled(false);
        crewAtButton.setEnabled(false);
        label.setText("ENTER USERNAME BELOW:");
        textField.setText("ANON");
        // Dont react to <CR>
        textField.removeActionListener(this);
        connected = false;
    }


    /* 
     * Called by Client to append text to JTextArea
     */
    void append(String message) {
        textArea.append(message);
        textArea.setCaretPosition(textArea.getText().length() - 1);
    }


    /*
     * Called when user clicks a button in order to figure out what to
     * do based on the button pressed.
     */
    public void actionPerformed(ActionEvent e) {

        Object obj = e.getSource();

        // LOG OUT button.
        if (obj == logoutButton) {
            client.sendMessage(new Message(Message.LOGOUT, ""));
            return;
        }
        
        // MAN WHERE MY CREW AT button.
        if (obj == crewAtButton) {
            client.sendMessage(new Message(Message.CREWAT, ""));
            return;
        }

        // JTextField.
        if (connected) {
            // Simply send the message!
            client.sendMessage(new Message(Message.MESSAGE,
                        textField.getText()));
            textField.setText("");
            return;
        }

        // LOG IN button, connection request.
        if (obj == loginButton) {
            String username = textField.getText().trim();
            // Ignore & return if no username entered.
            if (username.length() == 0) {
                return;
            }
            
            // Try creating new Client with GUI.
            client = new Client(username, this);
            if (!client.init()) {
                return;
            }
            
            // Clear the textfield for user input.
            textField.setText("");
            label.setText("ENTER YOUR MESSAGE BELOW:");
            connected = true;

            // Change availability of buttons.
            loginButton.setEnabled(false);
            logoutButton.setEnabled(true);
            crewAtButton.setEnabled(true);
            textField.addActionListener(this);
        }


    }


    public static void main(String[] args) {
        new ClientGUI();
    }



}
