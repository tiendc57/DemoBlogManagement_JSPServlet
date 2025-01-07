package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.entity.Blog;

public class BlogRepository extends Repository {
	public static BlogRepository instance;
	
	private BlogRepository() {
		
    }

	public static synchronized BlogRepository getInstance() {
        if (instance == null) {
            instance = new BlogRepository();
        }
        return instance;
    }
	
	public List<Blog> getAllBlogs() {
		List<Blog> blogs = new ArrayList<>();
		try (Connection connection = getOpenedSqlConnection()) {
			String query = "SELECT * FROM blogs";
			PreparedStatement stmt = connection.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Blog blog = new Blog();
				blog.setId(rs.getInt("id"));
				blog.setTitle(rs.getString("title"));
				blog.setContent(rs.getString("content"));
				blog.setAuthorId(rs.getInt("author_id"));
				blog.setCreatedAt(rs.getString("created_at"));
				blog.setUpdatedAt(rs.getString("updated_at"));
				blogs.add(blog);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blogs;
	}
}
