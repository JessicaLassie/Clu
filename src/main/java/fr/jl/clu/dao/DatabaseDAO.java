/*
 * Copyright (C) Jessica LASSIE from 2023 to present
 * All rights reserved
 */

package fr.jl.clu.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The database DAO
 */
public class DatabaseDAO {

    /**
     * Get the database tables
     *
     * @param cnx the database connection
     * @return the database tables
     * @throws SQLException
     */
    public static ResultSet getDatabaseTables(final Connection cnx) throws SQLException {
        String[] types = {"TABLE"};
        ResultSet tables = cnx.getMetaData().getTables(cnx.getCatalog(), null, "%", types);
        return tables;
    }

    /**
     * Get the data from selected table
     *
     * @param cnx           the database connection
     * @param selectedTable the selected table
     * @return the data of selected table
     * @throws SQLException
     */
    public static ResultSet getDataFromSelectedTable(final Connection cnx, final String selectedTable) throws SQLException {
        Statement stmt = cnx.createStatement();
        ResultSet dataTable = stmt.executeQuery("Select * from " + selectedTable);
        return dataTable;
    }
}
