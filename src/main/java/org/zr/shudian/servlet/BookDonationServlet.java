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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet("/api/books/donate")
public class BookDonationServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> result = new HashMap<>();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            result.put("success", false);
            result.put("message", "Please login to donate books");
        } else {
            try {
                Book book = gson.fromJson(req.getReader(), Book.class);
                
                // Generate a new ID for the donated book
                book.setId(UUID.randomUUID().toString());
                
                // Add the book to the store
                DataStore.addBook(book);
                
                result.put("success", true);
                result.put("message", "Book donated successfully");
                result.put("book", book);
            } catch (Exception e) {
                result.put("success", false);
                result.put("message", "Error processing donation: " + e.getMessage());
            }
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(result));
    }
} 