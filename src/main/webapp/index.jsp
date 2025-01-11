<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Blog Management</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/bootstrap5/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/bootstrap5/js/bootstrap.bundle.min.js">
</head>
<body>
	<%@ include file="navbar.jsp" %>
	
	<div class="container mt-4">
        <h1>Welcome to the Home Page</h1>
		<p>This is the content of the home page.</p>
    </div>
	
	<%@ include file="footer.jsp" %>
</body>
</html>