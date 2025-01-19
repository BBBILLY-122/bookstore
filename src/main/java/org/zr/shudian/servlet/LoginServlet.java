package org.zr.shudian.servlet;

import com.google.gson.Gson;
import org.zr.shudian.model.User;
import org.zr.shudian.service.DataStore;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/login")
@MultipartConfig
public class LoginServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> result = new HashMap<>();
        
        String idNumber = req.getParameter("idNumber");
        String password = req.getParameter("password");

        if (idNumber == null || password == null) {
            result.put("success", false);
            result.put("message", "ID number and password are required");
        } else {
            User user = DataStore.getUser(idNumber);
            if (user != null && user.getPassword().equals(password)) {
                req.getSession().setAttribute("user", user);
                result.put("success", true);
                result.put("message", "Login successful");
                result.put("redirect", user.getIdentity().equals("Admin") ? "/admin/dashboard" : "/user/dashboard");
            } else {
                result.put("success", false);
                result.put("message", "Invalid ID number or password");
            }
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(result));
    }
} 