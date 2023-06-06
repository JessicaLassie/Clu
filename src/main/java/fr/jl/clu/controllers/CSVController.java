/*
 * Copyright (C) Jessica LASSIE from 2023 to present
 * All rights reserved
 */

package fr.jl.clu.controllers;

import fr.jl.clu.dao.DatabaseDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * CSV controller
 */
public class CSVController {

    private static final Logger LOGGER = LogManager.getLogger(CSVController.class);
    private static final String DELIMITER = ",";
    private static final String SEPARATOR = "\n";

    /**
     * Export data from selected tables
     *
     * @param cnx                the database connection
     * @param outputPath         the output path for save the file
     * @param selectedTablesList the selected tables to export
     * @param databaseName       the database name
     * @throws SQLException
     * @throws IOException
     */
    public static void export(Connection cnx, String outputPath, ArrayList selectedTablesList, String databaseName) throws SQLException, IOException {
        LOGGER.info("Start export database " + databaseName + " table(s) : " + selectedTablesList);
        FileWriter file = new FileWriter(outputPath + "/" + databaseName + ".csv");
        for (int i = 0; i < selectedTablesList.size(); i++) {
            ResultSet dataTable = DatabaseDAO.getDataFromSelectedTable(cnx, selectedTablesList.get(i).toString());
            StringBuilder header = new StringBuilder();
            for (int j = 1; j <= dataTable.getMetaData().getColumnCount(); j++) {
                header.append(dataTable.getMetaData().getColumnName(j));
                if (j != dataTable.getMetaData().getColumnCount()) {
                    header.append(DELIMITER);
                }
            }
            header.append(SEPARATOR);
            file.append(header);
            StringBuilder content = new StringBuilder();
            while (dataTable.next()) {
                for (int k = 1; k <= dataTable.getMetaData().getColumnCount(); k++) {
                    content.append(dataTable.getObject(k));
                    if (k != dataTable.getMetaData().getColumnCount()) {
                        content.append(DELIMITER);
                    }
                }
                content.append(SEPARATOR);
            }
            file.append(content);
        }
        file.close();
        LOGGER.info("Finish ! Output file : " + outputPath + "/" + databaseName + ".csv");
    }
}
