/*
 * Copyright (C) Jessica LASSIE from 2023 to present
 * All rights reserved
 */

package fr.jl.clu.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connection controller
 */
public class ConnectionController {

    /**
     * Connect to the database
     *
     * @param driver   the database driver
     * @param server   the database server
     * @param port     the database port
     * @param database the database name
     * @param login    the database login
     * @param password the database password
     * @return the database connection
     * @throws SQLException
     */
    public static Connection getConnection(final String driver, final String server, final String port, final String database, final String login, final char[] password) throws SQLException {
        final String url = "jdbc:" + driver + "://" + server + ":" + port + "/" + database;
        return DriverManager.getConnection(url, login, new String(password));
    }

    /**
     * Disconnect to the database
     *
     * @param cnx the database connection
     * @return null
     * @throws SQLException
     */
    public static Connection disconnect(Connection cnx) throws SQLException {
        cnx.close();
        return null;
    }
}
