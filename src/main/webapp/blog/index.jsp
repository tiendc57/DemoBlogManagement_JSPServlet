<%@page import="data.response.BlogViewReponse"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div>
	<h1>Blog</h1>
	<div>
	<div>
	<%
		List<BlogViewReponse> blogs = (ArrayList<BlogViewReponse>) request.getAttribute("blogs");
		for (int i = 0; i < blogs.size(); i++)
		{
	%>
			<div class="card" style="width: 18rem;">
			  <img src="..." class="card-img-top" alt="...">
			  <div class="card-body">
			    <h5 class="card-title">${blogs[i].id}</h5>
			    <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
			    <a href="#" class="btn btn-primary">Go somewhere</a>
			  </div>
			</div>
	<%
		}
	%>
	</div>
	</div>
</div>
</body>
</html>