package org.irccom.sqlite;

import org.irccom.sqlite.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.Message;

public class UserGenericDaoImpl implements GenericDao<User, Integer> {
    private static final Logger logger = LogManager.getLogger(UserGenericDaoImpl.class);
    private final Optional<Connection> connection;

    public UserGenericDaoImpl() {
        this.connection = JdbcConnection.getConnection();
    }

    @Override
    public Optional<Integer> getId() {
        return Optional.empty();
    }

    @Override
    public Optional<User> get(int id) {
        return connection.flatMap(conn -> {
            Optional<User> user = Optional.empty();
            String sql = "SELECT * FROM user WHERE id = " + id;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    String nickname = resultSet.getString("nickname");
                    String alt_nickname = resultSet.getString("alt_nickname");
                    String real_name = resultSet.getString("real_name");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String channel = resultSet.getString("channel");

                    user = Optional.of(new User(id, nickname, alt_nickname, real_name, username , password, channel));

                    logger.log(Level.INFO,"Znaleziono {0} w bazie danych " + user.get());
                }
            } catch (SQLException ex) {
                logger.log(Level.ERROR, (Message) null, ex);
            }

            return user;
        });
    }

    @Override
    public Collection<User> getAll() {
        Collection<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nickname = resultSet.getString("nickname");
                    String alt_nickname = resultSet.getString("alt_nickname");
                    String real_name = resultSet.getString("real_name");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String channel = resultSet.getString("channel");

                    User user = new User(id, nickname, alt_nickname, real_name, username , password, channel);

                    users.add(user);

                    logger.log(Level.INFO, "Znaleziono {0} w bazie danych", user);
                }

            } catch (SQLException ex) {
                logger.log(Level.ERROR, (Message) null, ex);
            }
        });

        return users;
    }

    @Override
    public Optional<Integer> save(User user) {
        String message = "Użytkownik posiada puste wartości";
        User nonNullCustomer = Objects.requireNonNull(user, message);
        String sql = "INSERT INTO "
                + "user(id, nickname, alt_nickname, real_name, username, password ,channel) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";

        return connection.flatMap(conn -> {
            Optional<Integer> generatedId = Optional.empty();

            try (PreparedStatement statement = conn.prepareStatement(sql,
                    Statement.NO_GENERATED_KEYS)) {
                statement.setInt(1, nonNullCustomer.getId());
                statement.setString(2, nonNullCustomer.getNickname());
                statement.setString(3, nonNullCustomer.getAlt_nickname());
                statement.setString(4, nonNullCustomer.getRealname());
                statement.setString(5, nonNullCustomer.getUsername());
                statement.setString(6, nonNullCustomer.getPassword());
                statement.setString(7, nonNullCustomer.getChannels());

                int numberOfInsertedRows = statement.executeUpdate();

                logger.log(Level.INFO, "{0} został stworzony pomyślnie {1}",
                        new Object[]{nonNullCustomer, numberOfInsertedRows > 0});
            } catch (SQLException ex) {
                logger.log(Level.ERROR, (Message) null, ex);
            }
            return generatedId;
        });
    }

    @Override
    public void update(User user) {
        String message = "Aktualizowany serwer jest pusty";
        User nonNullCustomer = Objects.requireNonNull(user, message);
        String sql = "UPDATE user "
                + "SET "
                + "nickname = ?, "
                + "alt_nickname = ?, "
                + "real_name = ?, "
                + "username = ?, "
                + "password = ?, "
                + "channel = ? "
                + "WHERE "
                + "id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, nonNullCustomer.getNickname());
                statement.setString(2, nonNullCustomer.getAlt_nickname());
                statement.setString(3, nonNullCustomer.getRealname());
                statement.setString(4, nonNullCustomer.getUsername());
                statement.setString(5, nonNullCustomer.getPassword());
                statement.setString(6, nonNullCustomer.getChannels());
                statement.setInt(7, nonNullCustomer.getId());

                int numberOfUpdatedRows = statement.executeUpdate();

                logger.log(Level.INFO, "server został zaktualizowany pomyślnie {0}",
                        numberOfUpdatedRows > 0);

            } catch (SQLException ex) {
                logger.log(Level.ERROR, (Message) null, ex);
            }
        });
    }

    @Override
    public void delete(User user) {
        String message = "Usuwany serwer nie istnieje";
        User nonNullCustomer = Objects.requireNonNull(user, message);
        String sql = "DELETE FROM server WHERE id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setInt(1, nonNullCustomer.getId());

                int numberOfDeletedRows = statement.executeUpdate();

                logger.log(Level.INFO, "Server został usunięty pomyślnie {0}",
                        numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                logger.log(Level.ERROR, (Message) null, ex);
            }
        });
    }

}
