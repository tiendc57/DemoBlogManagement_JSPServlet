<%@page import="java.util.List"%>
<%@page import="data.response.BlogViewResponse"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Blog</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/bootstrap5/css/bootstrap.min.css">
</head>
<body>
	<%@ include file="./../navbar.jsp" %>

	<div class="container mt-5">
	    <h2><%= (request.getAttribute("formTitle") != null) ? request.getAttribute("formTitle") : "Create/Edit Blog" %></h2>
	    <form action="blog?action=create" method="post">
	        <input type="hidden" name="id" value="<%=(request.getAttribute("blog") != null) ? ((BlogViewResponse) request.getAttribute("blog")).getId() : ""%>">
	        <div class="form-group">
	            <label for="title">Title:</label>
	            <input type="text" class="form-control" id="title" name="title" 
	                   value="<%= (request.getAttribute("blog") != null) ? ((BlogViewResponse) request.getAttribute("blog")).getTitle() : ""%>" 
	                   required>
	        </div>
	        <div class="form-group">
			    <label for="category">Category:</label>
			    <select class="form-control" id="category" name="category" required>
			        <%
			            List<String> categories = (List<String>) request.getAttribute("categories");
			            String selectedCategory = request.getAttribute("blog") != null 
			                ? ((BlogViewResponse) request.getAttribute("blog")).getCategory() 
			                : ""; 
			        %>
			        <option value="" disabled <%= selectedCategory.isEmpty() ? "selected" : "" %>>-- Select a Category --</option>
			        <% 
			            if (categories != null) {
			                for (String category : categories) { 
			        %>
			            <option value="<%= category %>" <%= category.equals(selectedCategory) ? "selected" : "" %>><%= category %></option>
			        <%
			                }
			            }
			        %>
			    </select>
			</div>

	        <div class="form-group">
	            <label for="content">Content:</label>
	            <textarea class="form-control" id="content" name="content" rows="5" required>
	                <%= (request.getAttribute("blog") != null) ? ((BlogViewResponse) request.getAttribute("blog")).getContent() : ""%>
	            </textarea>
	        </div>
	        <div class="mt-2">
		        <button type="submit" class="btn btn-primary">Save</button>
		        <a href="<%= request.getContextPath() %>/blog?action=all" class="btn btn-secondary">Cancel</a>
	        </div>
	    </form>
	</div>
	
	<%@ include file="./../footer.jsp" %>
</body>
</html>