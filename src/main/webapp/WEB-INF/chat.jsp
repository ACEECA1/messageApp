<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.Classes.User" %>
<%
    String chatWith = (String) request.getAttribute("chatWith");
    String profilePic = (String) request.getAttribute("profilePic");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chat with <%= chatWith %></title>
</head>
<body>
    <h1>Chat with <%= chatWith %></h1>
    <img src="../uploads/<%= profilePic %>" alt="Profile Picture" width="100" height="100">
    <div id="messages"></div>
    <input type="text" id="messageInput" placeholder="Type a message...">
    <button id="sendBtn">Send</button>

    <script>
        const username = "<%= chatWith %>";
        const sender = "<%= ((User)request.getSession().getAttribute("user")).getUsername()%>";
        const messagesDiv = document.getElementById('messages');
        const input = document.getElementById('messageInput');
        const sendBtn = document.getElementById('sendBtn');

        const socket = new WebSocket("ws://192.168.100.3:8080/ws/chat?sender=" + encodeURIComponent(sender) + "&receiver=" + encodeURIComponent(username));

        socket.onopen = () => console.log('Connected');
        socket.onmessage = (event) => {
            const msg = document.createElement('div');
            msg.textContent = event.data;
            messagesDiv.appendChild(msg);
        };

        sendBtn.addEventListener('click', () => {
            socket.send(input.value);
            console.log('Sent: qsd', input.value);
            input.value = "";
        });
    </script>
</body>
</html>