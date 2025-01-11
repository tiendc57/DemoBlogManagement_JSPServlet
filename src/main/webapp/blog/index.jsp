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
		<div >		
			<h1 class="text-center">Blog Management</h1>
			<h4>
				Create blog:
				<span><a href="./blog?action=createForm">Create</a></span>
			</h4>
		</div>
		<div class="d-flex">
			<div class= "row">
			
				<%
				List<BlogViewResponse> blogs = (ArrayList<BlogViewResponse>) request.getAttribute("blogs");
				if(blogs != null) {
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
					<span>No blogs</span>
				<%
						}
				%>
			</div>
		</div>
	</div>
	<%@ include file="./../footer.jsp" %>
</div>
</body>
</html>