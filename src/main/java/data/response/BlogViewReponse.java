package data.response;

import java.util.UUID;

public class BlogViewReponse {
	private UUID id;
	private String category;
	private String title;
	private String content;
	private UUID authorId;
	private String createdAt;
	private String updatedAt;
	
	public BlogViewReponse() {
	}
	
	public BlogViewReponse(UUID id, String category, String title, String content, UUID authorId, String createdAt,
			String updatedAt) {
		super();
		this.id = id;
		this.category = category;
		this.title = title;
		this.content = content;
		this.authorId = authorId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UUID getAuthorId() {
		return authorId;
	}

	public void setAuthorId(UUID authorId) {
		this.authorId = authorId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
}
