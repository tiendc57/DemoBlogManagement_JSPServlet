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

	protected <T> List<Object> getSqlParameters(T entity) {
		List<Object> parameters = new ArrayList<>();
		Class<?> entityType = entity.getClass();
		Field[] fields = entityType.getDeclaredFields();

		for (Field field : fields) {
			try {
				field.setAccessible(true);
				Object value = field.get(entity);
				parameters.add(value);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Failed to access field value", e);
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
