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
import java.util.UUID;
import javax.servlet.annotation.MultipartConfig;

@WebServlet("/api/manage/books/*")
@MultipartConfig
public class BookManagementServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all books
            Collection<Book> books = DataStore.getAllBooks();
            sendJsonResponse(resp, true, "Books retrieved successfully", Map.of("books", books));
        } else {
            // Get specific book
            String bookId = pathInfo.substring(1);
            Book book = DataStore.getBook(bookId);
            if (book != null) {
                sendJsonResponse(resp, true, "Book retrieved successfully", Map.of("book", book));
            } else {
                sendJsonResponse(resp, false, "Book not found", null);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Book book = gson.fromJson(req.getReader(), Book.class);
            if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
                sendJsonResponse(resp, false, "Book title is required", null);
                return;
            }
            // Generate a new ID for the book
            book.setId(UUID.randomUUID().toString());
            DataStore.addBook(book);
            sendJsonResponse(resp, true, "Book added successfully", Map.of("book", book));
        } catch (Exception e) {
            sendJsonResponse(resp, false, "Error adding book: " + e.getMessage(), null);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            sendJsonResponse(resp, false, "Book ID is required", null);
            return;
        }
        String bookId = pathInfo.substring(1);
        
        try {
            Book book = gson.fromJson(req.getReader(), Book.class);
            if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
                sendJsonResponse(resp, false, "Book title is required", null);
                return;
            }
            book.setId(bookId);
            DataStore.updateBook(book);
            sendJsonResponse(resp, true, "Book updated successfully", Map.of("book", book));
        } catch (Exception e) {
            sendJsonResponse(resp, false, "Error updating book: " + e.getMessage(), null);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            sendJsonResponse(resp, false, "Book ID is required", null);
            return;
        }
        String bookId = pathInfo.substring(1);
        Book book = DataStore.getBook(bookId);
        if (book == null) {
            sendJsonResponse(resp, false, "Book not found", null);
            return;
        }
        DataStore.deleteBook(bookId);
        sendJsonResponse(resp, true, "Book deleted successfully", null);
    }

    private void sendJsonResponse(HttpServletResponse resp, boolean success, String message, Map<String, Object> data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        if (data != null) {
            response.putAll(data);
        }
        resp.getWriter().write(gson.toJson(response));
    }
} 