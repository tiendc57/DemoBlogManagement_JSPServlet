package repository;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConnectionDBConfig;

public abstract class Repository {
	private final String dbUrl;
	private final String dbUser;
	private final String dbPassword;
	protected Connection jdbcConnection;

	public Repository() {
		this.dbUrl = ConnectionDBConfig.DB_URL;
		this.dbUser = ConnectionDBConfig.DB_USER;
		this.dbPassword = ConnectionDBConfig.DB_PASSWORD;
	}

	protected Connection getOpenedSqlConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		connection.setAutoCommit(false);
		return connection;
	}

	protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                jdbcConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                jdbcConnection.setAutoCommit(false);
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
        }
    }

    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

	protected <T> List<Object> getSqlParameters(T entity) {
		List<Object> parameters = new ArrayList<>();
	    Field[] fields = entity.getClass().getDeclaredFields();

	    for (Field field : fields) {
	        field.setAccessible(true);
	        try {
	            Object value = field.get(entity);
	            parameters.add(value != null ? value : "");
	        } catch (IllegalAccessException e) {
	            System.err.println("Error accessing field: " + field.getName() + " - " + e.getMessage());
	        }
	    }
	    return parameters;
	}

	protected PreparedStatement createPreparedStatement(Connection connection, String sql, List<Object> parameters)
			throws SQLException {
		PreparedStatement statement = connection.prepareStatement(sql);
		for (int i = 0; i < parameters.size(); i++) {
			statement.setObject(i + 1, parameters.get(i));
		}
		return statement;
	}

}
