package com.projet;
import com.utils.Database;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.UUID;


@WebServlet("/register")
@MultipartConfig
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get parameters
        String email = request.getParameter("regEmail");
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String password = request.getParameter("regPassword");
        Part profilePicture = request.getPart("profilePic");

        String originalFileName = profilePicture.getSubmittedFileName();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        // Fix path construction - add File.separator after getRealPath
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadsDir = new File(uploadPath);

        // Create directory if it doesn't exist
        if (!uploadsDir.exists()) {
            uploadsDir.mkdirs(); // Use mkdirs() instead of mkdir()
        }

        // Write the file
        String filePath = uploadPath + File.separator + uniqueFileName;
            response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String dbUser = "root";
        String dbPassword = "0000";
        Database db = new Database(connectionUrl,dbUser,dbPassword);
        try {
            db.connect();
            boolean success = db.addUser(name, email, username, password , uniqueFileName);
            if (success) {
                profilePicture.write(filePath);
                out.println("<p style='color: green;'>✓ Registration successful!</p>");
                out.println("<p>Welcome, " + username + "!</p>");
            } else {
                out.println("<p style='color: red;'>✗ Registration failed!</p>");
            }
        } catch (SQLException e) {
            out.println("<p style='color: red;'>✗ Registration failed!</p>");
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