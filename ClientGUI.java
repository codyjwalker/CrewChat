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
import javax.swing.JTextPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.SimpleAttributeSet;


public class ClientGUI extends JFrame implements ActionListener {

    private static final String HOST = "crewchat.hopto.org";
    private static final int PORT = 36979;
//    private static final long serialVersionUID = -8054809742223994483L;
    private static final long serialVersionUID = -8054809829223994483L;

    private boolean connected;
    private JLabel label;
    private JTextField textField;
    private JButton loginButton, logoutButton, crewAtButton;
    private JTextPane textPane;
    private Client client;

    private StyledDocument doc;
    private SimpleAttributeSet keyword;
    private Color myGrey;

    public ClientGUI() {
        super("Chat Client");
        myGrey = new Color(48, 48, 48);

        // Input Panel: 3 grids wide, 1 grid tall.
        JPanel inputPanel = new JPanel(new GridLayout(3, 1));
        // Label.
        label = new JLabel("ENTER USERNAME BELOW:", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        inputPanel.add(label);
        label.setFont(new Font("Serif", Font.BOLD, 16));
        // TextField.
        textField = new JTextField("ANON");
        textField.setBackground(Color.WHITE);
        inputPanel.add(textField);
        inputPanel.setBackground(myGrey);
        inputPanel.setOpaque(true);
        add(inputPanel, BorderLayout.NORTH);

        // Center panel for the chatroom.
        textPane = new JTextPane();
        doc = textPane.getStyledDocument();
        keyword = new SimpleAttributeSet();
        StyleConstants.setForeground(keyword, Color.WHITE);
        StyleConstants.setFontSize(keyword, 16);
//        StyleConstants.setBold(keyword, true);
        JPanel chatPanel = new JPanel(new GridLayout(1, 1));
        chatPanel.add(new JScrollPane(textPane));
        textPane.setEditable(false);
        textPane.setBackground(myGrey);
        add(chatPanel, BorderLayout.CENTER);

        // The three buttons.
        // LOG IN.
        loginButton = new JButton("LOG IN");
        loginButton.setOpaque(false);
        loginButton.setBackground(myGrey);
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(this);
        // LOG OUT.
        logoutButton = new JButton("LOG OUT");
        logoutButton.setOpaque(false);
        logoutButton.setBackground(myGrey);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(this);
        logoutButton.setEnabled(false);     // Cant logout until logged in!
        // MANNNNN, WHERE MY CREW AT???
        crewAtButton = new JButton("MANNN, WHERE MY CREW AT?!?");
        crewAtButton.setEnabled(false);     // Not until logged in ya goof!
        crewAtButton.setOpaque(false);
        crewAtButton.setBackground(myGrey);
        crewAtButton.setForeground(Color.WHITE);
        crewAtButton.addActionListener(this);
        // Button panel.
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(crewAtButton);
        buttonPanel.setBackground(myGrey);
        buttonPanel.setOpaque(true);
        add(buttonPanel, BorderLayout.SOUTH);

        // Characteristics of Window.
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(myGrey);
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
//        textArea.append(message);
//        textArea.setCaretPosition(textArea.getText().length() - 1);
        try {
            doc.insertString(doc.getLength(), message, keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
