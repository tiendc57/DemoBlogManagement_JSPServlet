package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.entity.User;
import repository.UserRepository;

public class UserController extends HttpServlet {
	private UserRepository userRepository = new UserRepository();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (userRepository.validateUser(username, password)) {
                response.sendRedirect("blogList.jsp");
            } else {
                response.sendRedirect("login.jsp?error=1");
            }
        } else if ("register".equals(action)) {
            User user = new User();
            user.setUsername(request.getParameter("username"));
            user.setPassword(request.getParameter("password"));
            user.setEmail(request.getParameter("email"));
            userRepository.registerUser(user);
            response.sendRedirect("login.jsp");
        }
    }
}
