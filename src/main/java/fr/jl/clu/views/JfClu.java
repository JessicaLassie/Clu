/*
 * Copyright (C) Jessica LASSIE from 2023 to present
 * All rights reserved
 */

package fr.jl.clu.views;

import fr.jl.clu.controllers.CSVController;
import fr.jl.clu.controllers.ConnectionController;
import fr.jl.clu.dao.DatabaseDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The frame
 */
public class JfClu extends JFrame {

    private static final Logger LOGGER = LogManager.getLogger(JfClu.class);

    private Connection cnx;
    private final JPanel mainPanel;
    private JButton connectButton;
    private JTextField serverTextField;
    private JTextField portTextField;
    private JTextField databaseTextField;
    private JTextField loginTextField;
    private JPasswordField passwordTextField;
    private JList tablesList;

    public JfClu() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 1));
        mainPanel.add(createConnectionViewPanel());
        mainPanel.setBackground(Color.blue);
        setContentPane(mainPanel);
        setVisible(true);
    }

    /**
     * Create the connection panel
     *
     * @return the connection panel
     */
    private JPanel createConnectionViewPanel() {
        JLabel driverLabel = new JLabel("Driver *");
        String[] driversList = new String[]{"MySQL", "PostgreSQL"};
        JComboBox driverComboBox = new JComboBox(driversList);
        JPanel driverPanel = new JPanel(new BorderLayout());
        driverPanel.add(driverLabel, BorderLayout.CENTER);
        driverPanel.add(driverComboBox, BorderLayout.SOUTH);

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

        JPanel connectionViewPanel = new JPanel();
        connectionViewPanel.setLayout(new GridLayout(7, 1));
        connectionViewPanel.add(driverPanel);
        connectionViewPanel.add(serverPanel);
        connectionViewPanel.add(portPanel);
        connectionViewPanel.add(databasePanel);
        connectionViewPanel.add(loginPanel);
        connectionViewPanel.add(passwordPanel);
        connectionViewPanel.add(connectPanel);
        connectionViewPanel.setBorder(BorderFactory.createEmptyBorder(150, 350, 150, 350));

        checkMandatoryTextField(serverTextField);
        checkMandatoryTextField(portTextField);
        checkMandatoryTextField(databaseTextField);
        checkMandatoryTextField(loginTextField);
        checkMandatoryTextField(passwordTextField);

        connectButton.addActionListener(e -> {
            try {
                cnx = ConnectionController.getConnection(driverComboBox.getSelectedItem().toString().toLowerCase(), serverTextField.getText(), portTextField.getText(), databaseTextField.getText(), loginTextField.getText(), passwordTextField.getPassword());
                if (cnx != null) {
                    showPanel(createDatabaseViewPanel());
                    LOGGER.info("Connection success");
                }
            } catch (SQLException ex) {
                createDialog("Error", ex.getMessage());
                LOGGER.error(ex.getMessage());
            }
        });

        return connectionViewPanel;
    }

    /**
     * Create the database panel
     *
     * @return the database panel
     */
    private JPanel createDatabaseViewPanel() {

        JLabel databaseLabel = new JLabel("Database : ");
        JLabel databaseName = new JLabel(databaseTextField.getText());
        JPanel databasePanel = new JPanel(new BorderLayout());
        databasePanel.add(databaseLabel, BorderLayout.WEST);
        databasePanel.add(databaseName, BorderLayout.CENTER);

        JButton disconnectButton = new JButton("Disconnect");
        JPanel disconnectPanel = new JPanel();
        disconnectPanel.add(disconnectButton);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(databasePanel, BorderLayout.CENTER);
        headerPanel.add(disconnectPanel, BorderLayout.EAST);

        JLabel dtoLabel = new JLabel("dto");
        JPanel DTOPanel = new JPanel();
        DTOPanel.add(dtoLabel);

        JLabel daoLabel = new JLabel("dao");
        JPanel DAOPanel = new JPanel();
        DAOPanel.add(daoLabel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("DTO", DTOPanel);
        tabbedPane.add("DAO", DAOPanel);
        tabbedPane.add("CSV", createCSVViewPanel());

        JPanel databaseViewPanel = new JPanel(new BorderLayout());
        databaseViewPanel.add(headerPanel, BorderLayout.NORTH);
        databaseViewPanel.add(tabbedPane, BorderLayout.CENTER);
        databaseViewPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 30, 30));

        disconnectButton.addActionListener(e -> {
            try {
                cnx = ConnectionController.disconnect(cnx);
                if (cnx == null) {
                    showPanel(createConnectionViewPanel());
                    LOGGER.info("Disconnection success");
                }
            } catch (SQLException ex) {
                createDialog("Error", ex.getMessage());
                LOGGER.error(ex.getMessage());
            }
        });

        return databaseViewPanel;
    }

    /**
     * Create CSV panel
     *
     * @return the CSV panel
     */
    private JPanel createCSVViewPanel() {
        JLabel selectLabel = new JLabel("Select table(s) : ");
        JButton exportButton = new JButton("Export");
        JPanel exportPanel = new JPanel(new BorderLayout());
        exportPanel.add(selectLabel, BorderLayout.CENTER);
        exportPanel.add(exportButton, BorderLayout.EAST);

        JCheckBox selectAllCheckBox = new JCheckBox("Select all");
        JCheckBox deselectAllCheckBox = new JCheckBox("Deselect all");
        deselectAllCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        JPanel checkboxPanel = new JPanel(new BorderLayout());

        checkboxPanel.add(selectAllCheckBox, BorderLayout.WEST);
        checkboxPanel.add(deselectAllCheckBox, BorderLayout.CENTER);
        checkboxPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(exportPanel, BorderLayout.NORTH);
        northPanel.add(checkboxPanel, BorderLayout.CENTER);
        northPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel tablesListPanel = new JPanel();

        tablesList = new JList();
        try {
            ResultSet res = DatabaseDAO.getDatabaseTables(cnx);
            DefaultListModel<String> dlm = new DefaultListModel<>();
            while (res.next()) {
                String tableName = res.getString(3);
                dlm.addElement(tableName);
            }
            tablesList.setModel(dlm);
            tablesListPanel.add(tablesList);
        } catch (SQLException ex) {
            createDialog("Error", ex.getMessage());
            LOGGER.error(ex.getMessage());
        }

        centerPanel.add(tablesListPanel, BorderLayout.WEST);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel CSVPanel = new JPanel(new BorderLayout());
        CSVPanel.add(northPanel, BorderLayout.NORTH);
        CSVPanel.add(centerPanel, BorderLayout.CENTER);

        selectAllCheckBox.addActionListener(e -> {
            if (selectAllCheckBox.isSelected()) {
                deselectAllCheckBox.setSelected(false);
                tablesList.setSelectionInterval(0, tablesList.getModel().getSize() - 1);
            }
        });

        deselectAllCheckBox.addActionListener(e -> {
            if (deselectAllCheckBox.isSelected()) {
                selectAllCheckBox.setSelected(false);
                tablesList.clearSelection();
            }
        });

        tablesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectAllCheckBox.isSelected()) {
                    selectAllCheckBox.setSelected(false);
                }
                if (deselectAllCheckBox.isSelected()) {
                    deselectAllCheckBox.setSelected(false);
                }
            }
        });

        exportButton.addActionListener(e -> {
            if (tablesList.getSelectedValuesList().size() > 0) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (fileChooser.showSaveDialog(CSVPanel) == JFileChooser.APPROVE_OPTION) {
                    try {
                        String outputPath = fileChooser.getSelectedFile().getAbsolutePath();
                        ArrayList selectedTablesList = (ArrayList) tablesList.getSelectedValuesList();
                        CSVController.export(cnx, outputPath, selectedTablesList, databaseTextField.getText());
                    } catch (SQLException | IOException ex) {
                        createDialog("Error", ex.getMessage());
                        LOGGER.error(ex.getMessage());
                    }
                }
            } else {
                createDialog("Error", "Select a table");
            }
        });

        return CSVPanel;
    }

    /**
     * Create the error dialog
     *
     * @param type    the dialog type
     * @param message the error message
     */
    private void createDialog(String type, String message) {
        JDialog errorDialog = new JDialog();
        errorDialog.setTitle(type);
        JTextPane errorTextPane = new JTextPane();
        errorTextPane.setContentType("text/html");
        errorTextPane.setText("<html><center>" + message + "</center></html>");
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

    /**
     * Check if the text field is not empty
     *
     * @param textFieldToCheck the text field to check
     */
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
                connectButton.setEnabled(serverTextField.getText().length() > 0 && portTextField.getText().length() > 0 && databaseTextField.getText().length() > 0 && loginTextField.getText().length() > 0 && passwordTextField.getPassword().length > 0);
            }
        });
    }

    /**
     * Show panel
     *
     * @param panel the panel to show
     */
    private void showPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel);
        mainPanel.validate();
        mainPanel.repaint();
    }

}
