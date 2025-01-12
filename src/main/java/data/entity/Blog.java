package data.entity;

import java.time.LocalDateTime;

public class Blog {
	private String id; // UUID - varchar(36)
	private String category;
	private String title;
	private String content;
	private String authorId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Blog() {

	}

	public Blog(String id, String category, String title, String content, String authorId, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.category = category;
		this.title = title;
		this.content = content;
		this.authorId = authorId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
