package com.projet;
import com.utils.env;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import com.utils.Database;
import java.sql.SQLException;

import com.Classes.Message;

@ServerEndpoint("/ws/chat")
public class ChatSocket {

    // Map username -> list of sessions (for multiple tabs)
    private static ConcurrentHashMap<String, ArrayList<Session>> userSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        String sender = session.getRequestParameterMap().get("sender").get(0);
        String receiver = session.getRequestParameterMap().get("receiver").get(0);
        System.out.println("WebSocket opened for " + sender + " chatting with " + receiver);
        // Add this session to the sender's session list
        userSessions.putIfAbsent(sender, new ArrayList<>());
        userSessions.get(sender).add(session);
        String connectionUrl = env.getEnv("DB_URL");
        String dbUser = env.getEnv("DB_USER");
        String dbPassword = env.getEnv("DB_PASSWORD");
        // Send previous messages between sender and receiver
        
        Database db = new Database(connectionUrl, dbUser, dbPassword);

        try {
            db.connect();
            ArrayList<Message> messages = db.get50MessagesBetween(sender, receiver);
            try{
                for (Message msg : messages) {
                    session.getBasicRemote().sendText( msg.getSender() + " : " + msg.getContent());
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                db.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @OnMessage
    public void onMessage(String msg, Session session) {
        String sender = session.getRequestParameterMap().get("sender").get(0);
        String receiver = session.getRequestParameterMap().get("receiver").get(0);

        // Send to all sessions of the receiver
        ArrayList<Session> receiverSessions = userSessions.getOrDefault(receiver, new ArrayList<>());
        for (Session s : receiverSessions) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(sender + ": " + msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Send "You: ..." to all sender sessions except the current tab
        ArrayList<Session> senderSessions = userSessions.getOrDefault(sender, new ArrayList<>());
        for (Session s : senderSessions) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText("You: " + msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Save message to database
        String connectionUrl = "jdbc:mysql://localhost:3306/mydb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String dbUser = "root";
        String dbPassword = "0000";
        Database db = new Database(connectionUrl, dbUser, dbPassword);
        try {
            db.connect();
            db.saveMessage(sender, receiver, msg);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                db.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        // Remove this session from all user lists
        String sender = session.getRequestParameterMap().get("sender").get(0);
        String receiver = session.getRequestParameterMap().get("receiver").get(0);
        System.out.println("WebSocket closed between users " + sender + " and " + receiver);
        userSessions.forEach((user, sessions) -> sessions.remove(session));
        userSessions.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }
}
