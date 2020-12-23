package org.irccom.sqlite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.irccom.model.Server;

import java.sql.*;

public class Crud {
    private Connection conn;



    private static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:config.db" ;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public static ObservableList<Server> selectServers() throws SQLException {
        Statement stat = connect().createStatement();
        ObservableList<Server> servers = FXCollections.observableArrayList();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM server");
            int id;
            String name, ip, port;
            while(result.next()) {
                id = result.getInt("id");
                name = result.getString("name");
                ip = result.getString("ip");
                port = result.getString("port");
                servers.add(new Server(id, name, ip, port));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return servers;
    }
}
