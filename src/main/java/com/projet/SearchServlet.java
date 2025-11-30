package com.projet;
import com.utils.Database;
import com.Classes.User;
import com.utils.env;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import com.google.gson.Gson;


@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchUsername = request.getParameter("username");
        String connectionUrl = env.getEnv("DB_URL");
        String dbUser = env.getEnv("DB_USER");
        String dbPassword = env.getEnv("DB_PASSWORD");
        Database db = new Database(connectionUrl,dbUser,dbPassword);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            db.connect();
            ArrayList<User> users = db.searchUsersByUsername(searchUsername);
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(users);
            out.println(jsonResponse);
        } catch (SQLException e) {
            out.println("<p style='color: red;'>✗ Search failed!</p>");
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
