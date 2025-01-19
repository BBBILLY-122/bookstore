package org.zr.shudian.servlet;

import com.google.gson.Gson;
import org.zr.shudian.model.Book;
import org.zr.shudian.model.User;
import org.zr.shudian.service.DataStore;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/books/*")
public class BookServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> result = new HashMap<>();
        try {
            System.out.println("Fetching all books...");
            Collection<Book> books = DataStore.getAllBooks();
            System.out.println("Found " + books.size() + " books");
            
            result.put("success", true);
            result.put("books", books);
            System.out.println("Books data: " + gson.toJson(books));
        } catch (Exception e) {
            System.err.println("Error fetching books: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Error fetching books: " + e.getMessage());
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> result = new HashMap<>();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null && "Admin".equals(user.getIdentity())) {
            Book book = gson.fromJson(req.getReader(), Book.class);
            DataStore.addBook(book);
            result.put("success", true);
            result.put("message", "Book added successfully");
        } else {
            result.put("success", false);
            result.put("message", "Unauthorized");
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(result));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> result = new HashMap<>();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null && "Admin".equals(user.getIdentity())) {
            Book book = gson.fromJson(req.getReader(), Book.class);
            DataStore.updateBook(book);
            result.put("success", true);
            result.put("message", "Book updated successfully");
        } else {
            result.put("success", false);
            result.put("message", "Unauthorized");
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(result));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        Map<String, Object> result = new HashMap<>();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null && "Admin".equals(user.getIdentity())) {
            DataStore.deleteBook(id);
            result.put("success", true);
            result.put("message", "Book deleted successfully");
        } else {
            result.put("success", false);
            result.put("message", "Unauthorized");
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(result));
    }
} 