package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.entity.Blog;
import data.response.BlogViewResponse;
import repository.BlogRepository;

@WebServlet("/blog")
public class BlogController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BlogRepository blogRepository;

	@Override
	public void init() {
		blogRepository = BlogRepository.getInstance();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if(action == null) {
			action = "all";
		}
        try {
            switch (action) {
            case "all":
                viewBlog(request, response);
                break;
            case "createForm":
            	createBlogForm(request, response);
                break;
            case "create":
            	createBlog(request, response);
                break;
//            case "update":
//                updateBlog(request, response);
//                break;
//            case "delete":
//                deleteBlog(request, response);
//                break;
//            case "edit":
//                showEditForm(request, response);
//                break;
//            default:
//            	request.getRequestDispatcher("./webStatus/not-found.jsp");
//                break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void viewBlog(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<BlogViewResponse> blogs = blogRepository.getAllBlogs();
        request.setAttribute("blogs", blogs);
        RequestDispatcher dispatcher = request.getRequestDispatcher("./blog/index.jsp");
        dispatcher.forward(request, response);
    }

    private void createBlogForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
    	request.setAttribute("formTitle", "Create Blog");
    	request.setAttribute("categories", blogRepository.getAllCategories());
    	request.getRequestDispatcher("./blog/form.jsp").forward(request, response);
    }

    private void createBlog(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
    	String id = request.getParameter("id") == null || request.getParameter("id").isEmpty()
                ? UUID.randomUUID().toString()
                : request.getParameter("id");
    	String category = request.getParameter("category");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String authorId =request.getParameter("authorId");
        String createdAt = request.getParameter("createdAt");
        String updatedAt = request.getParameter("updatedAt");

        Blog newBlog = new Blog(id, category, title, content, authorId, createdAt, updatedAt);
        blogRepository.createBlog(newBlog);
        response.sendRedirect("viewBlog");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
    	String id = request.getParameter("id");
    	UUID blogId = UUID.fromString(id);
    	Blog blog = blogRepository.getBlogById(blogId);

    	request.setAttribute("blog", blog);
    	request.setAttribute("formTitle", "Edit Blog");
    	request.getRequestDispatcher("./blog/form.jsp").forward(request, response);

    }
//
//    private void updateBlog(HttpServletRequest request, HttpServletResponse response)
//            throws SQLException, IOException {
//        int id = Integer.parseInt(request.getParameter("id"));
//        String title = request.getParameter("title");
//        String author = request.getParameter("author");
//        float price = Float.parseFloat(request.getParameter("price"));
//
//        Blog book = new Blog(id, title, author, price);
//        blogRepository.updateBlog(book);
//        response.sendRedirect("list");
//    }
//
//    private void deleteBook(HttpServletRequest request, HttpServletResponse response)
//            throws SQLException, IOException {
//        int id = Integer.parseInt(request.getParameter("id"));
//
//        Blog book = new Blog(id);
//        blogRepository.deleteBlog(book);
//        response.sendRedirect("list");
//
//    }
}
