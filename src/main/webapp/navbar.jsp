<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <a class="navbar-brand" href="./index.jsp">Bee</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/blog?action=all">Blog</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/user">Account</a>
        </li>
      </ul>
      
      <div>
       <%
	       String userId = (session != null) ? (String) session.getAttribute("userId") : null;
		   if (userId == null) {
		%>
			<a href="<%= request.getContextPath() %>/auth?action=redirectLoginForm" style="text-decoration: none;">
				<button type="button" class="btn btn-outline-primary">Login</button>
			</a>
			<a href="<%= request.getContextPath() %>/auth?action=redirectRegisterForm" style="text-decoration: none;">
				<button type="button" class="btn btn-outline-primary">Register</button>
			</a>
		<%
		    } else {
		%>
		<div class="d-flex">
			<p class="text-white my-auto me-2">Welcome: <%= userId %></p>
			<a href="<%= request.getContextPath() %>/auth?action=logout" style="text-decoration: none;">
			    <button type="button" class="btn btn-outline-primary">Logout</button>
		    </a>
		</div>
		<%        
		    }
		%>
      </div>
    </div>
  </div>
</nav>