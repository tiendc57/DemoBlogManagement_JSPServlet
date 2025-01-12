package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

	public Blog getBlogById(String id) throws SQLException {
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

	public int countBlogs() throws SQLException {
	    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM blogs");
	    try (Connection conn = getOpenedSqlConnection();
	        PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	        }
	    }
	    return 0;
	}

	public List<BlogViewResponse> getFilterBlogs(int pageCurrent, int pageSize, String keywordSearch, String orderBy, Map<String, String> filters) throws SQLException {
	    StringBuilder sql = new StringBuilder("SELECT * FROM blogs WHERE 1=1");
	    List<Object> params = new ArrayList<>();
	    if (keywordSearch != null && !keywordSearch.isEmpty()) {
	        sql.append(" AND title LIKE ?");
	        params.add("%" + keywordSearch + "%");
	    }
	    if (filters != null && !filters.isEmpty()) {
	        for (Map.Entry<String, String> filter : filters.entrySet()) {
	            if (filter.getValue() != null && !filter.getValue().isEmpty()) {
	                sql.append(" AND ").append(filter.getKey()).append(" = ?");
	                params.add(filter.getValue());
	            }
	        }
	    }
	    if (orderBy != null && !orderBy.isEmpty()) {
	        sql.append(" ORDER BY ").append(orderBy);
	    } else {
	        sql.append(" ORDER BY created_at DESC");
	    }
	    sql.append(" LIMIT ? OFFSET ?");
	    params.add(pageSize);
	    params.add((pageCurrent - 1) * pageSize);
	    List<BlogViewResponse> blogs = new ArrayList<>();
	    try (Connection conn = getOpenedSqlConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
	        for (int i = 0; i < params.size(); i++) {
	            stmt.setObject(i + 1, params.get(i));
	        }
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Blog blog = readEntity(rs);
	                blogs.add(convertToRecord(blog));
	            }
	        }
	    }
	    return blogs;
	}

	public int countFilterBlogs(String keywordSearch, Map<String, String> filters) throws SQLException {
	    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM blogs WHERE 1=1");
	    List<Object> params = new ArrayList<>();

	    if (keywordSearch != null && !keywordSearch.isEmpty()) {
	        sql.append(" AND title LIKE ?");
	        params.add("%" + keywordSearch + "%");
	    }

	    if (filters != null && !filters.isEmpty()) {
	        for (Map.Entry<String, String> filter : filters.entrySet()) {
	            if (filter.getValue() != null && !filter.getValue().isEmpty()) {
	                sql.append(" AND ").append(filter.getKey()).append(" = ?");
	                params.add(filter.getValue());
	            }
	        }
	    }
	    try (Connection conn = getOpenedSqlConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
	        for (int i = 0; i < params.size(); i++) {
	            stmt.setObject(i + 1, params.get(i));
	        }
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	        }
	    }
	    return 0;
	}


	public boolean createBlog(Blog blog) throws SQLException {
	    String sql = "INSERT INTO blogs (id, category, title, content, author_id, created_at, updated_at) VALUES (?,?,?,?,?,?,?)";  
	    try (Connection conn = getOpenedSqlConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, blog.getId());
	        stmt.setString(2, blog.getCategory());
	        stmt.setString(3, blog.getTitle());
	        stmt.setString(4, blog.getContent());
	        stmt.setString(5, blog.getAuthorId());
	        stmt.setTimestamp(6, java.sql.Timestamp.valueOf(blog.getCreatedAt()));
	        stmt.setTimestamp(7, java.sql.Timestamp.valueOf(blog.getUpdatedAt()));
	        return stmt.executeUpdate() > 0;
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
        entity.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        entity.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
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
