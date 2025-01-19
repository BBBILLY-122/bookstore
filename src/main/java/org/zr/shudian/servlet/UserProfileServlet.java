package org.zr.shudian.servlet;

import com.google.gson.Gson;
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

@WebServlet("/api/profile")
public class UserProfileServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> result = new HashMap<>();
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            result.put("success", true);
            result.put("user", user);
        } else {
            result.put("success", false);
            result.put("message", "Not logged in");
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(result));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> result = new HashMap<>();
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser != null) {
            User updatedUser = gson.fromJson(req.getReader(), User.class);
            // 保持一些关键字段不变
            updatedUser.setId(currentUser.getId());
            updatedUser.setIdNumber(currentUser.getIdNumber());
            updatedUser.setIdentity(currentUser.getIdentity());
            
            DataStore.updateUser(updatedUser);
            session.setAttribute("user", updatedUser);
            
            result.put("success", true);
            result.put("message", "Profile updated successfully");
        } else {
            result.put("success", false);
            result.put("message", "Not logged in");
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(result));
    }
} 