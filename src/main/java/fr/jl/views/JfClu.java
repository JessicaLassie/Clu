/*
 * Copyright (C) Jessica LASSIE from 2023 to present
 * All rights reserved
 */

package fr.jl.views;

import fr.jl.controllers.ConnectionController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 */
public class JfClu extends JFrame {

    private JfClu frame;
    private Connection cnx;
    private JButton connectButton;
    private JTextField serverTextField;
    private JTextField portTextField;
    private JTextField databaseTextField;
    private JTextField loginTextField;
    private JPasswordField passwordTextField;
    public JfClu() {

        JLabel serverLabel = new JLabel("Server *");
        serverTextField = new JTextField();
        JPanel serverPanel = new JPanel(new BorderLayout());
        serverPanel.add(serverLabel, BorderLayout.CENTER);
        serverPanel.add(serverTextField, BorderLayout.SOUTH);

        JLabel portLabel = new JLabel("Port *");
        portTextField = new JTextField();
        JPanel portPanel = new JPanel(new BorderLayout());
        portPanel.add(portLabel, BorderLayout.CENTER);
        portPanel.add(portTextField, BorderLayout.SOUTH);

        JLabel databaseLabel = new JLabel("Database *");
        databaseTextField = new JTextField();
        JPanel databasePanel = new JPanel(new BorderLayout());
        databasePanel.add(databaseLabel, BorderLayout.CENTER);
        databasePanel.add(databaseTextField, BorderLayout.SOUTH);

        JLabel loginLabel = new JLabel("Login *");
        loginTextField = new JTextField();
        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.add(loginLabel, BorderLayout.CENTER);
        loginPanel.add(loginTextField, BorderLayout.SOUTH);

        JLabel passwordLabel = new JLabel("Password *");
        passwordTextField = new JPasswordField();
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.add(passwordLabel, BorderLayout.CENTER);
        passwordPanel.add(passwordTextField, BorderLayout.SOUTH);

        connectButton = new JButton("Connect");
        connectButton.setEnabled(false);
        JPanel connectPanel = new JPanel();
        connectPanel.add(connectButton);
        connectPanel.setBorder(BorderFactory.createEmptyBorder(15,0,0,0));

        JPanel connectionViewPanel = new JPanel();
        connectionViewPanel.setLayout(new GridLayout(6,1));
        connectionViewPanel.add(serverPanel);
        connectionViewPanel.add(portPanel);
        connectionViewPanel.add(databasePanel);
        connectionViewPanel.add(loginPanel);
        connectionViewPanel.add(passwordPanel);
        connectionViewPanel.add(connectPanel);
        connectionViewPanel.setBorder(BorderFactory.createEmptyBorder(120,350,120,350));

        JLabel databaseName = new JLabel();
        JButton disconnectButton = new JButton("Disconnect");
        JPanel databaseViewPanel = new JPanel();
        databaseViewPanel.add(databaseName);
        databaseViewPanel.add(disconnectButton);

        setContentPane(connectionViewPanel);
        setVisible(true);

        checkMandatoryTextField(serverTextField);
        checkMandatoryTextField(portTextField);
        checkMandatoryTextField(databaseTextField);
        checkMandatoryTextField(loginTextField);
        checkMandatoryTextField(passwordTextField);



        connectButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    cnx = ConnectionController.getConnection(serverTextField.getText(), portTextField.getText(), databaseTextField.getText(), loginTextField.getText(), passwordTextField.getPassword());
                    connectionViewPanel.setVisible(false);
                    databaseName.setText(databaseTextField.getText());
                    setContentPane(databaseViewPanel);
                } catch (SQLException ex) {
                    System.out.println(ex.getCause());
                    JDialog errorDialog = new JDialog();
                    errorDialog.setTitle("Error");
                    JTextPane errorMessage = new JTextPane();
                    errorMessage.setText(ex.getMessage());
                    errorMessage.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
                    errorDialog.add(errorMessage);
                    errorDialog.setSize(600,150);
                    int xDiff = (connectionViewPanel.getWidth() - errorDialog.getWidth()) / 2;
                    int x = ((int) connectionViewPanel.getLocationOnScreen().getX()) + xDiff;
                    int yDiff = (connectionViewPanel.getHeight() - errorDialog.getHeight()) / 2;
                    int y = ((int) connectionViewPanel.getLocationOnScreen().getY()) + yDiff;
                    errorDialog.setLocation(x, y);
                    errorDialog.setVisible(true);
                }
            }
        });

        disconnectButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    cnx = ConnectionController.disconnect(cnx);
                    databaseViewPanel.setVisible(false);
                    setContentPane(connectionViewPanel);
                    connectionViewPanel.setVisible(true);
                } catch (SQLException ex) {
                    System.out.println(ex.getCause());
                    JDialog errorDialog = new JDialog();
                    errorDialog.setTitle("Error");
                    JTextPane errorMessage = new JTextPane();
                    errorMessage.setText(ex.getMessage());
                    errorMessage.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
                    errorDialog.add(errorMessage);
                    errorDialog.setSize(600,150);
                    int xDiff = (connectionViewPanel.getWidth() - errorDialog.getWidth()) / 2;
                    int x = ((int) connectionViewPanel.getLocationOnScreen().getX()) + xDiff;
                    int yDiff = (connectionViewPanel.getHeight() - errorDialog.getHeight()) / 2;
                    int y = ((int) connectionViewPanel.getLocationOnScreen().getY()) + yDiff;
                    errorDialog.setLocation(x, y);
                    errorDialog.setVisible(true);
                }
            }
        });

    }
    private void checkMandatoryTextField(JTextField textFieldToCheck) {
        textFieldToCheck.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                checkMandatory();
            }
            public void removeUpdate(DocumentEvent e) {
                checkMandatory();
            }
            public void insertUpdate(DocumentEvent e) {
                checkMandatory();
            }
            private void checkMandatory() {
                if (serverTextField.getText().length()>0 && portTextField.getText().length()>0 && databaseTextField.getText().length()>0 && loginTextField.getText().length()>0 && passwordTextField.getText().length()>0){
                    connectButton.setEnabled(true);
                } else {
                    connectButton.setEnabled(false);
                }
            }
        });
    }

}
