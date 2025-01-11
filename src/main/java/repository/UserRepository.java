package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import data.entity.Blog;
import data.entity.User;
import data.response.BlogViewResponse;
import data.response.UserViewResponse;

public class UserRepository extends Repository{
	public static UserRepository instance;

	private UserRepository() {

    }

	public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

	public boolean validateUser(String username, String password) {
        try (Connection connection = getOpenedSqlConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerUser(User user) {
        try (Connection connection = getOpenedSqlConnection()) {
            String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public UserViewResponse getUserByUsername(String name) throws SQLException {
		String query = "SELECT * FROM users WHERE username = ?";
		UserViewResponse userViewResponse = null;
		try (Connection conn = getOpenedSqlConnection();
		    PreparedStatement stmt = conn.prepareStatement(query)) {
		    stmt.setObject(1, name);
		    try (ResultSet rs = stmt.executeQuery()) {
		        if (rs.next()) {
		            User user = readEntity(rs);
		            userViewResponse = convertToRecord(user);
		        }
		    }
		}
		return userViewResponse;
	}
    
    
    private User readEntity(ResultSet rs) throws SQLException {
        User entity = new User();
        entity.setId(rs.getString("id"));
        entity.setName(rs.getString("name"));
        entity.setUsername(rs.getString("username"));
        entity.setPassword(rs.getString("password"));
        entity.setEmail(rs.getString("email"));
        return entity;
    }

    private UserViewResponse convertToRecord(User entity) {
        return new UserViewResponse(
                entity.getId(),
                entity.getName(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail()
        );
    }

    private User convertToEntity(UserViewResponse record) {
        if (record.getId() == null || record.getId().toString().isEmpty()) {
            record.setId(UUID.randomUUID().toString());
        }
        User entity = new User();
        entity.setId(record.getId());
        entity.setName(record.getName());
        entity.setUsername(record.getUsername());
        entity.setPassword(record.getPassword());
        entity.setEmail(record.getEmail());
        return entity;
    }
}
