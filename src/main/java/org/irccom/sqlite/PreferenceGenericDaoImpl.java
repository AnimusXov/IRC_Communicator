package org.irccom.sqlite;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.irccom.sqlite.model.Preference;
import org.irccom.sqlite.model.Server;
import org.irccom.sqlite.model.User;

import java.sql.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.prefs.Preferences;

public class PreferenceGenericDaoImpl implements GenericDao<Preference, Integer>{
private static final Logger logger = LogManager.getLogger(ServerGenericDaoImpl.class);

private final Optional<Connection> connection;

public PreferenceGenericDaoImpl() {
	this.connection = JdbcConnection.getConnection();
}


@Override
public Optional<Integer> getId(){
	return Optional.empty();
}

@Override
public Optional<Preference> get( int id) {
	return connection.flatMap(conn -> {
		Optional<Preference> preference = Optional.empty();
		String sql = "SELECT * FROM preference WHERE id = " + id;
		
		try (Statement statement = conn.createStatement();
		     ResultSet resultSet = statement.executeQuery(sql)) {
			
			if (resultSet.next()) {
				String color = resultSet.getString("color");
				preference = Optional.of(new Preference(id,color));
				
				logger.info("Znaleziono {0} w bazie danych" + preference.get());
			}
		} catch ( SQLException ex) {
			logger.log(Level.FATAL, (Message) null, ex);
		}
		
		return preference;
	});
}

@Override
public Collection<Preference> getAll(){
	return null;
}

@Override
public Optional<Integer> save( Preference preference ){
	String message = "Preferencja posiada puste wartości";
	Preference nonNullCustomer = Objects.requireNonNull(preference, message);
	String sql = "INSERT INTO "
			             + "preference(id,color) "
			             + "VALUES(?,?)";
	
	return connection.flatMap(conn -> {
		Optional<Integer> generatedId = Optional.empty();
		
		try (PreparedStatement statement = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS)) {
			
			statement.setInt(1, nonNullCustomer.getId());
			statement.setString(2, nonNullCustomer.getColor());
	
			
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
		return generatedId;
	});
}

@Override
public void update( Preference preference ){
	String message = "Aktualizowana preferencja jest pusta";
	Preference nonNullCustomer = Objects.requireNonNull(preference, message);
	String sql = "UPDATE preference "
			             + "SET "
			             + "color = ? "
			             + "WHERE "
			             + "id = ?";
	connection.ifPresent(conn -> {
		try (PreparedStatement statement = conn.prepareStatement(sql)) {
			
			statement.setString(1, nonNullCustomer.getColor());
			statement.setInt(2, nonNullCustomer.getId());
			
			int numberOfUpdatedRows = statement.executeUpdate();
			
			logger.log(Level.INFO, "server został zaktualizowany pomyślnie {0}");
			
		} catch (SQLException ex) {
			logger.log(Level.ERROR, (Message) null, ex);
		}
	});
	
}

@Override
public void delete( Preference preference ){
	String message = "Usuwany serwer nie istnieje";
	Preference nonNullCustomer = Objects.requireNonNull(preference, message);
	String sql = "DELETE FROM preference WHERE id = ?";
	
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
