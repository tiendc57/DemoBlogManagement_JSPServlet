<%@page import="data.response.BlogViewResponse"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
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
<div>
	<%@ include file="./../navbar.jsp" %>
	<div class="container">
		<% 
			if (session.getAttribute("userId") != null) {
		%>
		<div>		
			<h1 class="text-center">Blog Management</h1>
			<h4>
				Create blog:
				<span><a href="./blog?action=createForm">Create</a></span>
			</h4>
		</div>
		<%
		    }
		%>
		
		<!-- Filter Form -->
		<form action="<%= request.getContextPath() %>/blog" method="get" class="row g-3 my-4">
			<input type="hidden" name="action" value="filter"> <!-- match action in switch case -->
		    <div class="col-md-4">
		        <label for="search" class="form-label">Search</label>
		        <input type="text" class="form-control" id="search" name="search" 
		               value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>">
		    </div>
		    <div class="col-md-4">
		        <label for="category" class="form-label">Category</label>
		        <select id="category" name="category" class="form-select">
		            <option value="">All Categories</option>
		            <% List<String> categories = (List<String>) request.getAttribute("categories"); %>
		            <% if (categories != null) { %>
		                <% for (String category : categories) { %>
		                    <option value="<%= category %>" 
		                        <%= category.equals(request.getParameter("category")) ? "selected" : "" %>><%= category %></option>
		                <% } %>
		            <% } %>
		        </select>
		    </div>
		    <div class="col-md-4 d-flex align-items-end">
		        <button type="submit" class="btn btn-primary">Filter</button>
		    </div>
		</form>
		
		<!-- Blog List -->
		<hr>
		<div class="w-100">
			<div class="row w-100">
				<%
				List<BlogViewResponse> blogs = (ArrayList<BlogViewResponse>) request.getAttribute("blogs");
				if(blogs != null && !blogs.isEmpty()) {
					for(BlogViewResponse blog : blogs)
					{
				%>
				<div class="col-4 my-2">
					<div class="card">
					  <img src="..." class="card-img-top" alt="...">
					  <div class="card-body">
					    <h5 class="card-title text-truncate"><%= blog.getTitle() %></h5>
					    <p class="card-text text-truncate"><%= blog.getContent() %></p>
					    <a href="#" class="btn btn-primary">View blog</a>
					  </div>
					</div>
				</div>
				<%
					}
				} else {
				%>
					<span>No blogs found</span>
				<%
				}
				%>
			</div>
		</div>
		
		<!-- Pagination -->
		<nav aria-label="Page navigation" class="my-4">
		    <ul class="pagination justify-content-center">
		        <%
		        int totalPages = (int) request.getAttribute("totalPages");
		        int currentPage = (int) request.getAttribute("currentPage");
		        for (int i = 1; i <= totalPages; i++) {
		        %>
		            <li class="page-item <%= i == currentPage ? "active" : "" %>">
		                <a class="page-link" href="./blog?action=filter&page=<%= i %>&search=<%= request.getParameter("search") %>&category=<%= request.getParameter("category") %>"><%= i %></a>
		            </li>
		        <%
		        }
		        %>
		    </ul>
		</nav>
	</div>
	<%@ include file="./../footer.jsp" %>
</div>


</body>
</html>