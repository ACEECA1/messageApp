package com.utils;
import java.sql.*;
import java.util.ArrayList;
import com.Classes.User;
import com.Classes.Message;

public class Database {
    private String url;
    private String user;
    private String password;
    private Connection connection;
    public Database(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
    public Connection getConnection() throws SQLException {
        return connection;
    }
    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
    }
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    public boolean addUser(String displayName, String email, String username, String password , String profilePicPath) throws SQLException {
        String sql = "INSERT INTO Users (displayname, email, username, password , profilePic) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, displayName);
            pstmt.setString(2, email);
            pstmt.setString(3, username);
            pstmt.setString(4, password);
            pstmt.setString(5, profilePicPath);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    public String getUsernameByEmail(String email) throws SQLException {
        String sql = "SELECT username FROM Users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                } else {
                    return null;
                }
            }
        }
    }
    public int getUserIdByEmail(String email) throws SQLException {
        String sql = "SELECT UserId FROM Users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("UserId");
                } else {
                    return -1; // or throw an exception
                }
            }
        }
    }
    public String getProfilePicByEmail(String email) throws SQLException {
        String sql = "SELECT profilePic FROM Users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("profilePic");
                } else {
                    return null;
                }
            }
        }
    }
    public String getProfilePicByUsername(String username) throws SQLException {
        String sql = "SELECT profilePic FROM Users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("profilePic");
                } else {
                    return null;
                }
            }
        }
    }
    public boolean validateUser(String email, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    public ResultSet runQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }
    public ArrayList<User> searchUsersByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Users WHERE username LIKE ?";
        ArrayList<User> users = new ArrayList<>();
        if(username != null && !username.isEmpty()){

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1,username + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        User user = new User(
                                rs.getInt("UserId"),
                                rs.getString("displayname"),
                                rs.getString("email"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("profilePic")
                        );
                        users.add(user);
                    }
                }
            }
        }
        return users;
    }
    public ArrayList<Message> get50MessagesBetween(String user1, String user2) throws SQLException {
        String sql = "SELECT * FROM messages WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) ORDER BY timestamp ASC LIMIT 50";
        ArrayList<Message> messages = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user1);
            pstmt.setString(2, user2);
            pstmt.setString(3, user2);
            pstmt.setString(4, user1);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Message msg = new Message(
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getString("message"),
                        rs.getString("timestamp")
                    );
                    messages.add(msg);
                }
            }
        }
        return messages;
    }
    public void saveMessage(String sender, String receiver, String message) throws SQLException {
        String sql = "INSERT INTO messages (sender, receiver, message, timestamp) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, sender);
            pstmt.setString(2, receiver);
            pstmt.setString(3, message);
            pstmt.executeUpdate();
        }
    }
}
