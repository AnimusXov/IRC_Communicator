package org.irccom.sqlite;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.Message;
import org.irccom.sqlite.model.Server;

import java.sql.*;
import java.util.*;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




public class ServerGenericDaoImpl implements GenericDao<Server, Integer> {
    private static final Logger logger = LogManager.getLogger(ServerGenericDaoImpl.class);
    public Optional<Integer> lastGeneratedId;
    private final Optional<Connection> connection;

    public ServerGenericDaoImpl() {
        this.connection = JdbcConnection.getConnection();
    }

    public Optional<Integer> getId() {
        return lastGeneratedId;
    }

    @Override
    public Optional<Server> get(int id) {
        return connection.flatMap(conn -> {
            Optional<Server> server = Optional.empty();
            String sql = "SELECT * FROM server WHERE id = " + id;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String ip = resultSet.getString("ip");
                    String port = resultSet.getString("port");

                    server = Optional.of(new Server(id, name, ip, port));

                    logger.info("Znaleziono {0} w bazie danych" + server.get());
                }
            } catch (SQLException ex) {
                logger.log(Level.FATAL, (Message) null, ex);
            }

            return server;
        });
    }

    @Override
    public Collection<Server> getAll() {
        Collection<Server> customers = new ArrayList<>();
        String sql = "SELECT * FROM server";

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String ip = resultSet.getString("ip");
                    String port = resultSet.getString("port");

                    Server server = new Server(id, name, ip, port);

                    customers.add(server);

                    logger.log(Level.INFO, "Znaleziono {0} w bazie danych", server);
                }

            } catch (SQLException ex) {
                logger.log(Level.ERROR, (Message) null, ex);
            }
        });

        return customers;
    }

    @Override
    public Optional<Integer> save(Server customer) {
        String message = "Serwer posiada puste wartości";
        Server nonNullCustomer = Objects.requireNonNull(customer, message);
        String sql = "INSERT INTO "
                + "server(name, ip, port) "
                + "VALUES(?, ?, ?)";

        return connection.flatMap(conn -> {
            Optional<Integer> generatedId = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, nonNullCustomer.getName());
                statement.setString(2, nonNullCustomer.getIp());
                statement.setString(3, nonNullCustomer.getPort());

                int numberOfInsertedRows = statement.executeUpdate();

                // Get the automatically generated ID
                if (numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            generatedId = Optional.of(resultSet.getInt(1));
                        }
                    }
                }

                logger.log(Level.INFO, "{0} został stworzony pomyślnie {1}" +
                        Arrays.toString(new Object[]{nonNullCustomer}));
            } catch (SQLException ex) {
                logger.log(Level.ERROR, (Message) null, ex);
            }
            lastGeneratedId = generatedId;
            return generatedId;
        });
    }

    @Override
    public void update(Server server) {
        String message = "Aktualizowany serwer jest pusty";
        Server nonNullCustomer = Objects.requireNonNull(server, message);
        String sql = "UPDATE server "
                + "SET "
                + "name = ?, "
                + "ip = ?, "
                + "port = ? "
                + "WHERE "
                + "id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, nonNullCustomer.getName());
                statement.setString(2, nonNullCustomer.getIp());
                statement.setString(3, nonNullCustomer.getPort());
                statement.setInt(4, nonNullCustomer.getId());

                int numberOfUpdatedRows = statement.executeUpdate();

                logger.log(Level.INFO, "server został zaktualizowany pomyślnie {0}");

            } catch (SQLException ex) {
                logger.log(Level.ERROR, (Message) null, ex);
            }
        });
    }

    @Override
    public void delete(Server server) {
        String message = "Usuwany serwer nie istnieje";
        Server nonNullCustomer = Objects.requireNonNull(server, message);
        String sql = "DELETE FROM server WHERE id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setInt(1, nonNullCustomer.getId());

                int numberOfDeletedRows = statement.executeUpdate();

                logger.log(Level.INFO, "Server został usunięty pomyślnie {0}"
                        );

            } catch (SQLException ex) {
                logger.log(Level.ERROR, (Message) null, ex);
            }
        });
    }


}
