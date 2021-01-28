package org.irccom.sqlite;

import java.sql.*;

public class Initializer {
    public static Statement stat;
    static Connection conn = null;
    String dbName;

    public Initializer() {
    }

    public Initializer(String databaseName){
        try {
            // Parameters of database
            String url = "jdbc:sqlite:" + databaseName;
            // Create a connection with the database
            conn = DriverManager.getConnection(url);
            dbName = databaseName;
            System.out.println("Połączenie z SQLite zostało nawiązane.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public  void createNewTable(String databaseName) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + databaseName;

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS server (\n"
                + " id integer PRIMARY KEY,\n"
                + " name text NOT NULL,\n"
                + " ip text NOT NULL,\n"
                + " port text NOT NULL \n"
                + ");";

        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
