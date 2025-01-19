package org.zr.shudian.filter;

import com.google.gson.Gson;
import org.zr.shudian.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter("/api/*")
public class ApiAuthFilter implements Filter {
    private final Gson gson = new Gson();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Allow access to login and register endpoints
        if (path.startsWith("/api/login") || path.startsWith("/api/register")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        // Check if user is logged in
        if (user == null) {
            sendUnauthorizedResponse(resp, "Not logged in");
            return;
        }

        // Check admin access for management endpoints
        if (path.startsWith("/api/manage/")) {
            if (!"Admin".equals(user.getIdentity())) {
                sendUnauthorizedResponse(resp, "Admin access required");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", message);
        response.getWriter().write(new Gson().toJson(result));
    }
} 