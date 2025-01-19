package org.zr.shudian.servlet;

import com.google.gson.Gson;
import org.zr.shudian.model.Book;
import org.zr.shudian.model.Order;
import org.zr.shudian.model.User;
import org.zr.shudian.service.DataStore;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@WebServlet("/api/checkout")
public class CheckoutServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> result = new HashMap<>();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            result.put("success", false);
            result.put("message", "Not logged in");
        } else {
            try {
                Map<String, Object> requestData = gson.fromJson(req.getReader(), Map.class);
                List<Map<String, Object>> items = (List<Map<String, Object>>) requestData.get("items");
                
                // Validate stock and calculate total
                double totalAmount = 0;
                List<Order.OrderItem> orderItems = new ArrayList<>();
                boolean stockAvailable = true;
                String outOfStockBook = "";

                for (Map<String, Object> item : items) {
                    String bookId = (String) item.get("id");
                    int quantity = ((Double) item.get("quantity")).intValue();
                    Book book = DataStore.getBook(bookId);

                    if (book.getStock() < quantity) {
                        stockAvailable = false;
                        outOfStockBook = book.getTitle();
                        break;
                    }

                    Order.OrderItem orderItem = new Order.OrderItem();
                    orderItem.setBookId(bookId);
                    orderItem.setQuantity(quantity);
                    orderItem.setPrice(book.getPrice());
                    orderItems.add(orderItem);
                    totalAmount += book.getPrice() * quantity;
                }

                if (!stockAvailable) {
                    result.put("success", false);
                    result.put("message", "Insufficient stock for book: " + outOfStockBook);
                } else {
                    // Create order
                    Order order = new Order();
                    order.setId(UUID.randomUUID().toString());
                    order.setUserId(user.getId());
                    order.setItems(orderItems);
                    order.setTotalAmount(totalAmount);
                    order.setStatus("Pending");

                    // Update stock
                    for (Order.OrderItem item : orderItems) {
                        Book book = DataStore.getBook(item.getBookId());
                        book.setStock(book.getStock() - item.getQuantity());
                        DataStore.updateBook(book);
                    }

                    // Save order
                    DataStore.addOrder(order);

                    result.put("success", true);
                    result.put("message", "Order placed successfully");
                    result.put("orderId", order.getId());
                }
            } catch (Exception e) {
                result.put("success", false);
                result.put("message", "Error processing order: " + e.getMessage());
            }
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(result));
    }
} 