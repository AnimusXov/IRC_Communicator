package org.irccom.sqlite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.irccom.model.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class GenericDaoImpl implements GenericDao<Server, Integer> {

    private final Optional<Connection> connection;

    public GenericDaoImpl() {
        this.connection = JdbcConnection.getConnection();
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

                    LOGGER.log(Level.INFO, "Znaleziono {0} w bazie danych", server.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
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

                    LOGGER.log(Level.INFO, "Znaleziono {0} w bazie danych", server);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
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

                LOGGER.log(Level.INFO, "{0} został stworzony pomyślnie {1}",
                        new Object[]{nonNullCustomer, numberOfInsertedRows > 0});
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

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

                LOGGER.log(Level.INFO, "server został zaktualizowany pomyślnie {0}",
                        numberOfUpdatedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
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

                LOGGER.log(Level.INFO, "Server został usunięty pomyślnie {0}",
                        numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

}
