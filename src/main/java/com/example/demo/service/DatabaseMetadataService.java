package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.sql.*;

@Service
public class DatabaseMetadataService {

    public void parseDatabaseMetadata(String url, String databaseName, String username, String password) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Get database metadata
            DatabaseMetaData metaData = connection.getMetaData();

            // Get all tables in the database
            String[] types = { "TABLE" };
            ResultSet tables = metaData.getTables(null, null, "%", types);

            // Iterate through the tables
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Table: " + tableName);
                // Get columns for each table
                ResultSet columns = metaData.getColumns(databaseName, null, tableName, "%");
                System.out.println("\tColumns: ");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");
                    int columnSize = columns.getInt("COLUMN_SIZE");
                    String columnDefaultValue = columns.getString("COLUMN_DEF");
                    System.out.println(String.format("\t\tName: %s | Type: %s | Size: %s | Default value: %s ", columnName, columnType, columnSize, columnDefaultValue));
                }
                parsePrimaryKeys(metaData, databaseName, tableName);
                parseForeignKeys(metaData, databaseName, tableName);
                System.out.println("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parsePrimaryKeys(DatabaseMetaData metaData, String databaseName, String tableName) throws SQLException {
        ResultSet pkResultSet = metaData.getPrimaryKeys(databaseName, null, tableName);
        while (pkResultSet.next()) {
            String pkColumnName = pkResultSet.getString("COLUMN_NAME");
            System.out.println(String.format("\tPrimary key: %s", pkColumnName));
        }
    }

    private void parseForeignKeys(DatabaseMetaData metaData, String databaseName, String tableName) throws SQLException {
        ResultSet fkResultSet  = metaData.getImportedKeys(databaseName, null, tableName);
        while (fkResultSet.next()){
            String fkColumnName = fkResultSet.getString("FKCOLUMN_NAME");
            String pkTableName = fkResultSet.getString("PKTABLE_NAME");
            String pkColumnName = fkResultSet.getString("PKCOLUMN_NAME");
            System.out.println(String.format("\tForeign key: %s (reference to %s.%s)", fkColumnName, pkTableName, pkColumnName));
        }
        fkResultSet.close();
    }
}
