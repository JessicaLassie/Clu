/*
 * Copyright (C) Jessica LASSIE from 2023 to present
 * All rights reserved
 */

package fr.jl.clu.views;

import fr.jl.clu.controllers.ConnectionController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger LOGGER = LogManager.getLogger(JfClu.class);

    private Connection cnx;
    private JPanel mainPanel;
    private JButton connectButton;
    private JTextField serverTextField;
    private JTextField portTextField;
    private JTextField databaseTextField;
    private JTextField loginTextField;
    private JPasswordField passwordTextField;

    public JfClu() {

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 1));
        mainPanel.add(createConnectionPanel());
        mainPanel.setBackground(Color.blue);
        setContentPane(mainPanel);
        setVisible(true);

    }

    private JPanel createConnectionPanel() {
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
        connectPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel connectionPanel = new JPanel();
        connectionPanel.setLayout(new GridLayout(6, 1));
        connectionPanel.add(serverPanel);
        connectionPanel.add(portPanel);
        connectionPanel.add(databasePanel);
        connectionPanel.add(loginPanel);
        connectionPanel.add(passwordPanel);
        connectionPanel.add(connectPanel);
        connectionPanel.setBorder(BorderFactory.createEmptyBorder(150, 350, 150, 350));

        checkMandatoryTextField(serverTextField);
        checkMandatoryTextField(portTextField);
        checkMandatoryTextField(databaseTextField);
        checkMandatoryTextField(loginTextField);
        checkMandatoryTextField(passwordTextField);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cnx = ConnectionController.getConnection(serverTextField.getText(), portTextField.getText(), databaseTextField.getText(), loginTextField.getText(), passwordTextField.getPassword());
                    if (cnx != null) {
                        showPanel(createDatabasePanel());
                        LOGGER.info("Connection success");
                    }
                } catch (SQLException ex) {
                    createErrorDialog(ex.getMessage());
                    LOGGER.error(ex.getMessage());
                }
            }
        });

        return connectionPanel;
    }

    private JPanel createDatabasePanel() {
        JLabel databaseName = new JLabel(databaseTextField.getText());
        JButton disconnectButton = new JButton("Disconnect");
        JPanel databasePanel = new JPanel();
        databasePanel.add(databaseName);
        databasePanel.add(disconnectButton);

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cnx = ConnectionController.disconnect(cnx);
                    if (cnx == null) {
                        showPanel(createConnectionPanel());
                        LOGGER.info("Disconnection success");
                    }
                } catch (SQLException ex) {
                    createErrorDialog(ex.getMessage());
                    LOGGER.info(ex.getMessage());
                }
            }
        });

        return databasePanel;
    }

    private void createErrorDialog(String errorMessage) {
        JDialog errorDialog = new JDialog();
        errorDialog.setTitle("Error");
        JTextPane errorTextPane = new JTextPane();
        errorTextPane.setContentType("text/html");
        errorTextPane.setText("<html><center>" + errorMessage + "</center></html>");
        errorTextPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        errorTextPane.setEditable(false);
        errorDialog.add(errorTextPane);
        errorDialog.setSize(600, 125);
        int xDiff = (mainPanel.getWidth() - errorDialog.getWidth()) / 2;
        int x = ((int) mainPanel.getLocationOnScreen().getX()) + xDiff;
        int yDiff = (mainPanel.getHeight() - errorDialog.getHeight()) / 2;
        int y = ((int) mainPanel.getLocationOnScreen().getY()) + yDiff;
        errorDialog.setLocation(x, y);
        errorDialog.setVisible(true);
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
                if (serverTextField.getText().length() > 0 && portTextField.getText().length() > 0 && databaseTextField.getText().length() > 0 && loginTextField.getText().length() > 0 && passwordTextField.getPassword().length > 0) {
                    connectButton.setEnabled(true);
                } else {
                    connectButton.setEnabled(false);
                }
            }
        });
    }

    private void showPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel);
        mainPanel.validate();
        mainPanel.repaint();
    }

}
