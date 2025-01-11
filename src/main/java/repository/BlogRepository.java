package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import data.entity.Blog;
import data.entityEnum.BlogCategory;
import data.response.BlogViewResponse;


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

	public List<String> getAllCategories() {
		return Arrays.stream(BlogCategory.values())
                .map(Enum::name)
                .collect(Collectors.toList());
	}

	public List<BlogViewResponse> getAllBlogs() {
		List<BlogViewResponse> blogs = new ArrayList<>();
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

	public Blog getBlogById(UUID id) throws SQLException {
		String query = "SELECT * FROM blogs WHERE id = ?";
		Blog blog = null;
		try (Connection conn = getOpenedSqlConnection();
		     PreparedStatement stmt = conn.prepareStatement(query)) {
		    stmt.setObject(1, id);
		    try (ResultSet rs = stmt.executeQuery()) {
		        if (rs.next()) {
		            blog = readEntity(rs);
		        }
		    }
		}
		return blog;
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

	public boolean createBlog(Blog blog) throws SQLException {
        String sql = "INSERT INTO blogs (id, category, title, content, author_id, created_at, updated_at) VALUES (?,?,?,?,?,?,?,?)";
        List<Object> parameters = List.of(
                blog.getId(),
                blog.getCategory(),
                blog.getTitle(),
                blog.getContent(),
                blog.getAuthorId().toString(),
                blog.getCreatedAt(),
                blog.getUpdatedAt()
            );
        try (PreparedStatement statement = createPreparedStatement(jdbcConnection, sql, parameters)) {
            return statement.executeUpdate() > 0;
        }
    }

	public boolean updateBlog(Blog blog) throws SQLException {
        String sql = "UPDATE blogs SET category = ?, title = ?, content = ?, updated_at = ? WHERE id = ?";
        List<Object> parameters = List.of(
            blog.getCategory(),
            blog.getTitle(),
            blog.getContent(),
            blog.getUpdatedAt(),
            blog.getId().toString()
        );
        try (PreparedStatement statement = createPreparedStatement(jdbcConnection, sql, parameters)) {
            return statement.executeUpdate() > 0;
        }
    }

	 public boolean deleteBlog(UUID id) throws SQLException {
	        String sql = "DELETE FROM blogs WHERE id = ?";
	        try (PreparedStatement statement = createPreparedStatement(jdbcConnection, sql, List.of(id.toString()))) {
	            return statement.executeUpdate() > 0;
	        }
	    }

	private Blog readEntity(ResultSet rs) throws SQLException {
        Blog entity = new Blog();
        entity.setId(rs.getString("id"));
        entity.setCategory(rs.getString("category"));
        entity.setTitle(rs.getString("title"));
        entity.setContent(rs.getString("content"));
        entity.setAuthorId(rs.getString("author_id"));
        entity.setCreatedAt(rs.getString("created_at"));
        entity.setUpdatedAt(rs.getString("updated_at"));
        return entity;
    }

    private BlogViewResponse convertToRecord(Blog entity) {
        return new BlogViewResponse(
                entity.getId(),
                entity.getCategory(),
                entity.getTitle(),
                entity.getContent(),
                entity.getAuthorId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private Blog convertToEntity(BlogViewResponse record) {
        if (record.getId() == null || record.getId().toString().isEmpty()) {
            record.setId(UUID.randomUUID().toString());
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
