package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import data.entity.Blog;
import data.response.BlogViewReponse;


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

	public List<BlogViewReponse> getAllBlogs() {
		List<BlogViewReponse> blogs = new ArrayList<>();
		try (Connection connection = getOpenedSqlConnection()) {
			String query = "SELECT * FROM blogs";
			PreparedStatement stmt = connection.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Blog blog = readEntity(rs);
				blogs.add(convertToRecord(blog));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blogs;
	}
	
//	public List<Blog> getAllBlogsByAuthorId(UUID authorId) throws SQLException {
//        String query = "SELECT * FROM blogs WHERE author_id = ?";
//        List<Blog> records = new ArrayList<>();
//        try (Connection conn = getOpenedSqlConnection();
//            PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setObject(1, authorId);
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                	Blog entity = readEntity(rs);
//                    records.add(convertToRecord(entity));
//                }
//            }
//        }
//        return records;
//    }
	
	private Blog readEntity(ResultSet rs) throws SQLException {
        Blog entity = new Blog();
        entity.setId(UUID.fromString(rs.getString("id")));
        entity.setCategory(rs.getString("category"));
        entity.setTitle(rs.getString("title"));
        entity.setContent(rs.getString("content"));
        entity.setAuthorId(UUID.fromString(rs.getString("author_id")));
        entity.setCreatedAt(rs.getString("created_at"));
        entity.setUpdatedAt(rs.getString("updated_at"));
        return entity;
    }

    private BlogViewReponse convertToRecord(Blog entity) {
        return new BlogViewReponse(
                entity.getId(),
                entity.getCategory(),
                entity.getTitle(),
                entity.getContent(),
                entity.getAuthorId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private Blog convertToEntity(BlogViewReponse record) {
        if (record.getId() == null || record.getId().toString().isEmpty()) {
            record.setId(UUID.randomUUID());
        }
        Blog entity = new Blog();
        entity.setId(record.getId());
        entity.setCategory(record.getCategory());
        entity.setTitle(record.getTitle());
        entity.setContent(record.getContent());
        entity.setAuthorId(record.getAuthorId());
        entity.setCreatedAt(record.getCreatedAt());
        entity.setUpdatedAt(record.getUpdatedAt());
        return entity;
    }
}
