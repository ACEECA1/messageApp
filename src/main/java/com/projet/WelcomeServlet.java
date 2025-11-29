package com.projet;
import com.Classes.User;
import com.utils.Database;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            out.println("<h1>Welcome to the Welcome Page!" + user.getUsername() +"</h1>");
            out.println("<img src='uploads/" + user.getProfilePicture() + "' alt='Profile Picture' width='150' height='150'>");
        }
        else {
            response.sendRedirect("index.html");
        }
    }
}

