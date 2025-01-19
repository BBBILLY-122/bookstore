package org.zr.shudian.servlet;

import com.google.gson.Gson;
import org.zr.shudian.model.User;
import org.zr.shudian.service.DataStore;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet("/api/register")
@MultipartConfig
public class RegisterServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String name = req.getParameter("name");
            String gender = req.getParameter("gender");
            int age = Integer.parseInt(req.getParameter("age"));
            String identity = req.getParameter("identity");
            String idNumber = req.getParameter("idNumber");
            String phone = req.getParameter("phone");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            Map<String, Object> result = new HashMap<>();

            if (DataStore.getUser(idNumber) != null) {
                result.put("success", false);
                result.put("message", "ID number already exists");
            } else {
                User user = new User(
                    UUID.randomUUID().toString(),
                    name,
                    gender,
                    age,
                    identity,
                    idNumber,
                    phone,
                    email,
                    password
                );

                DataStore.addUser(user);

                result.put("success", true);
                result.put("message", "Registration successful");
            }

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(gson.toJson(result));
        } catch (NumberFormatException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "Invalid age format");
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(gson.toJson(result));
        }
    }
} 