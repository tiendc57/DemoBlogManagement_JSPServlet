package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.entity.Blog;
import repository.BlogRepository;

@WebServlet("/blog")
public class BlogController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BlogRepository blogRepository;
	
	public void init() {
		blogRepository = BlogRepository.getInstance();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request,response);
	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        try {
            switch (action) {
            case "?/detail":
                viewBlog(request, response);
                break;
            case "/create":
                createBlog(request, response);
                break;
            case "/update":
                updateBlog(request, response);
                break;
            case "/delete":
                deleteBlog(request, response);
                break;
            case "/edit":
                showEditForm(request, response);
                break;
            default:
            	request.getRequestDispatcher("NotFound.jsp");
                break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void viewBlog(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Blog> listBlog = blogRepository.getAllBlogs();
        request.setAttribute("listBlog", listBlog);
        RequestDispatcher dispatcher = request.getRequestDispatcher("BlogList.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("BlogForm.jsp");
        dispatcher.forward(request, response);
    }
 
    private void createBlog(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        float price = Float.parseFloat(request.getParameter("price"));
 
        Blog newBlog = new Blog(title, author, price);
        blogRepository.createBook(newBlog);
        response.sendRedirect("list");
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Blog existingBook = blogRepository.getBlog(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("BlogForm.jsp");
        request.setAttribute("book", existingBook);
        dispatcher.forward(request, response);
 
    }
 
    private void updateBlog(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        float price = Float.parseFloat(request.getParameter("price"));
 
        Blog book = new Blog(id, title, author, price);
        blogRepository.updateBlog(book);
        response.sendRedirect("list");
    }
 
    private void deleteBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
 
        Blog book = new Blog(id);
        blogRepository.deleteBlog(book);
        response.sendRedirect("list");
 
    }
}
