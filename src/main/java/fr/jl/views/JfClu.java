/*
 * Copyright (C) Jessica LASSIE from 2023 to present
 * All rights reserved
 */

package fr.jl.views;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class JfClu extends JFrame {
    public JfClu() {
        super("Clu");

        JLabel serverLabel = new JLabel("Server *");
        JTextField server = new JTextField();

        JLabel portLabel = new JLabel("Port *");
        JTextField port = new JTextField();

        JLabel databaseLabel = new JLabel("Database *");
        JTextField database = new JTextField();

        JLabel loginLabel = new JLabel("Login *");
        JTextField login = new JTextField();

        JLabel passwordLabel = new JLabel("Password *");
        JTextField password = new JPasswordField();

        JButton connectButton = new JButton("Connect");

        JPanel serverPanel = new JPanel(new BorderLayout());
        serverPanel.add(serverLabel, BorderLayout.CENTER);
        serverPanel.add(server, BorderLayout.SOUTH);

        JPanel portPanel = new JPanel(new BorderLayout());
        portPanel.add(portLabel, BorderLayout.CENTER);
        portPanel.add(port, BorderLayout.SOUTH);

        JPanel databasePanel = new JPanel(new BorderLayout());
        databasePanel.add(databaseLabel, BorderLayout.CENTER);
        databasePanel.add(database, BorderLayout.SOUTH);

        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.add(loginLabel, BorderLayout.CENTER);
        loginPanel.add(login, BorderLayout.SOUTH);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.add(passwordLabel, BorderLayout.CENTER);
        passwordPanel.add(password, BorderLayout.SOUTH);

        JPanel connectPanel = new JPanel();
        connectPanel.add(connectButton);
        connectPanel.setBorder(BorderFactory.createEmptyBorder(15,0,0,0));

        JPanel connectionPanel = new JPanel();
        connectionPanel.setLayout(new GridLayout(6,1));
        connectionPanel.add(serverPanel);
        connectionPanel.add(portPanel);
        connectionPanel.add(databasePanel);
        connectionPanel.add(loginPanel);
        connectionPanel.add(passwordPanel);
        connectionPanel.add(connectPanel);
        connectionPanel.setBorder(BorderFactory.createEmptyBorder(80,250,80,250));

        setContentPane(connectionPanel);
        setVisible(true);
    }

}
