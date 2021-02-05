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
        String sql = "create table preference\n"
		                     + "(\n" + "    id    int not null\n" +
		                     "        constraint preference_pk\n" +
		                     "            primary key,\n" + "    color text\n" + ");\n" + "\n" +
		                     "create table server\n" + "(\n" + "    id   integer\n" +
		                     "        primary key,\n" + "    name text not null,\n" +
		                     "    ip   text not null,\n" + "    port text not null\n" + ");\n" +
		                     "\n" + "create table user\n" + "(\n" + "    id           int not null\n" +
		                     "        references server,\n" + "    nickname     text,\n" +
		                     "    alt_nickname text,\n" + "    real_name    text,\n" + "    username     text,\n" +
		                     "    password     text,\n" + "    channel      text\n" + ");\n" + "\n";
        

        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
