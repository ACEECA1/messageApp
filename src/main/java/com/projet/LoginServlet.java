package com.projet;
import com.Classes.User;
import com.utils.Database;
import com.utils.env;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String connectionUrl = env.getEnv("DB_URL");
        String dbUser = env.getEnv("DB_USER");
        String dbPassword = env.getEnv("DB_PASSWORD");
        
        Database db = new Database(connectionUrl,dbUser,dbPassword);
        try {
            db.connect();
            boolean valid = db.validateUser(email, password);
            String username = db.getUsernameByEmail(email);
            if (valid) {
                String pfp = db.getProfilePicByEmail(email);
                HttpSession session = request.getSession();
                User user = new User(db.getUserIdByEmail(email), username, email, username, password, pfp);
                session.setAttribute("user", user);
                //redirect
                response.sendRedirect("message.html");
            } else {
                out.println("<p style='color: red;'>✗ Invalid email or password!</p>");
            }
        } catch (SQLException e) {
            out.println("<p style='color: red;'>✗ Login failed!</p>");
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        } finally {
            try {
                db.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}