package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.entity.User;
import data.response.UserViewResponse;
import repository.UserRepository;

@WebServlet("/auth")
public class AuthController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserRepository userRepository;

	@Override
	public void init() {
		userRepository = UserRepository.getInstance();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
        	switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "redirectLoginForm":
            	redirectLoginForm(request, response);
                break;
            case "register":
            	handleRegister(request, response);
                break;
            case "redirectRegisterForm":
            	redirectRegisterForm(request, response);
                break;
            case "logout":
            	HttpSession session = request.getSession(false);
            	session.removeAttribute("userId");
            	response.sendRedirect(request.getContextPath() + "/index.jsp");
                break;
            default:
            	response.sendRedirect(request.getContextPath() + "/webStatus/not-found.jsp");
                break;
            }
        } catch (Exception ex) {
        	throw new ServletException(ex);
		}
    }

	private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession(false);
        if (userRepository.validateUser(username, password)) {
        	UserViewResponse userViewResponse = userRepository.getUserByUsername(username);
            session.setAttribute("userId", userViewResponse.getId());
            response.sendRedirect(request.getContextPath() + "/blog?action=all");
        } else {
            response.sendRedirect(request.getContextPath() + "/auth?action=redirectLoginForm&error=1");
        }
    }

	private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
		User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setEmail(request.getParameter("email"));
        if (userRepository.registerUser(user)) {
            response.sendRedirect(request.getContextPath() + "/auth?action=redirectLoginForm");
        } else {
            response.sendRedirect(request.getContextPath() + "/auth?action=redirectRegisterForm&error=1");
        }
        response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
    }

	private void redirectLoginForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
    	request.getRequestDispatcher("/auth/login.jsp").forward(request, response);

    }

	private void redirectRegisterForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
    	request.getRequestDispatcher("/auth/register.jsp").forward(request, response);

    }
}
