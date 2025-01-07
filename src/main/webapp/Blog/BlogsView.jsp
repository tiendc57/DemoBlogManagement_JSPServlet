<%@page import="java.util.ArrayList"%>
<%@page import="model.entity.Blog"%>
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
	<table>
	<%
		List<Blog> blogs = (ArrayList<Blog>) request.getAttribute("blogs");
		for (int i = 0; i < blogs.size(); i++)
		{
	%>
			<tr>
				<td><%= blogs.get(i).getId() %> </td>
				<td><%= blogs.get(i).getTitle() %> </td>
				<td><%= blogs.get(i).getContent() %> </td>
				<td><%= blogs.get(i).getAuthorId() %> </td>
				<td><%= blogs.get(i).getCategory() %> </td>
				<td><%= blogs.get(i).getCreatedAt() %> </td>
				<td><%= blogs.get(i).getUpdatedAt() %> </td>
			</tr>
	<%
		}
	%>
	</table>
	</div>
</div>
</body>
</html>