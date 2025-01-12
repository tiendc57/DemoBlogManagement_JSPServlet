package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.entity.Blog;
import data.response.BlogViewResponse;
import repository.BlogRepository;

@WebServlet("/blog")
public class BlogController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BlogRepository blogRepository;
	private int pageSize = 6;

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
            case "filter":
            	handleFilter(request, response);
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
        int totalBlogs = blogRepository.countBlogs();
        int totalPages = (int) Math.ceil((double) totalBlogs / pageSize);

        request.setAttribute("blogs", blogs);
        request.setAttribute("categories", blogRepository.getAllCategories());
        request.setAttribute("currentPage", 1);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("blogs", blogs);
        RequestDispatcher dispatcher = request.getRequestDispatcher("./blog/index.jsp");
        dispatcher.forward(request, response);
    }

    private void handleFilter(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String searchToken = request.getParameter("search");
        int page = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int pageSize = 6;
        String category = request.getParameter("category");
        String orderBy = request.getParameter("orderBy");

        Map<String, String> filters = new HashMap<>();
        if (category != null && !category.isEmpty()) {
            filters.put("category", category);
        }
        List<BlogViewResponse> blogs = blogRepository.getFilterBlogs(page, pageSize, searchToken, orderBy, filters);

        int totalBlogs = blogRepository.countFilterBlogs(searchToken, filters);
        int totalPages = (int) Math.ceil((double) totalBlogs / pageSize);

        request.setAttribute("blogs", blogs);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("categories", blogRepository.getAllCategories());
        request.setAttribute("searchToken", searchToken);
        request.setAttribute("selectedCategory", category);
        request.setAttribute("orderBy", orderBy);

        request.getRequestDispatcher("/blog/index.jsp").forward(request, response);
    }



    private void createBlogForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
    	request.setAttribute("formTitle", "Create Blog");
    	request.setAttribute("categories", blogRepository.getAllCategories());
    	request.getRequestDispatcher("./blog/form.jsp").forward(request, response);
    }

    private void createBlog(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
    	HttpSession session = request.getSession(false);
    	String id = request.getParameter("id") == null || request.getParameter("id").isEmpty()
                ? UUID.randomUUID().toString()
                : request.getParameter("id");
    	String category = request.getParameter("category");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String authorId = session.getAttribute("userId").toString();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        Blog newBlog = new Blog(id, category, title, content, authorId, createdAt, updatedAt);
        boolean createdFlag = blogRepository.createBlog(newBlog);
        request.setAttribute("createdFlag", createdFlag);

        request.getRequestDispatcher("/blog/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
    	String id = request.getParameter("id");
    	Blog blog = blogRepository.getBlogById(id);

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
