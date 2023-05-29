/*
 * Copyright (C) Jessica LASSIE from 2023 to present
 * All rights reserved
 */

package fr.jl.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*

 */
public class ConnectionController {

    public static Connection getConnection(final String server, final String port, final String database, final String login,  final char[] password) throws SQLException {
        final String url = "jdbc:mysql://" + server + ":" + port + "/" + database;
        return DriverManager.getConnection(url, login, new String(password));
    }

    public static Connection disconnect(Connection cnx) throws SQLException {
        cnx.close();
        return null;
    }
}
