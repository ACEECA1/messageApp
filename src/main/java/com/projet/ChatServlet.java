package com.projet;
import com.utils.Database;
import com.utils.env;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
// import java.io.PrintWriter;
// import java.sql.*;
// import java.util.ArrayList;
// import com.google.gson.Gson;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Security check: Redirect to login if no session exists
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
            return;
        }
        
        String chatWith = request.getParameter("user");
        String connectionUrl = env.getEnv("DB_URL");
        String dbUser = env.getEnv("DB_USER");
        String dbPassword = env.getEnv("DB_PASSWORD");
        Database db = new Database(connectionUrl, dbUser, dbPassword);
        String profilePic = "";
        try {
            db.connect();
            profilePic = db.getProfilePicByUsername(chatWith);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                db.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        request.setAttribute("chatWith", chatWith);
        request.setAttribute("profilePic", profilePic);

        // Forward to JSP page
        request.getRequestDispatcher("/WEB-INF/chat.jsp").forward(request, response);
    }
}