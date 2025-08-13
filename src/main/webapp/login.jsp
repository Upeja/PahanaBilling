<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>🔐 Login - Pahana Bookshop</title>
    <style>
        /* Dark theme background */
        body {
            background: #121212;
            color: #fff;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        /* Login card */
        .login-container {
            background: #1e1e1e;
            padding: 40px 30px;
            border-radius: 12px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.5);
            width: 350px;
            text-align: center;
        }

        h2 {
            color: #ffb74d;
            margin-bottom: 30px;
            font-size: 28px;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 12px 15px;
            margin: 10px 0;
            border-radius: 6px;
            border: none;
            background: #2c2c2c;
            color: #fff;
            font-size: 16px;
        }

        input::placeholder {
            color: #aaa;
        }

        button {
            background: #ffb74d;
            color: #121212;
            border: none;
            padding: 12px 20px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: bold;
            width: 100%;
            margin-top: 15px;
            transition: 0.3s;
            font-size: 16px;
        }

        button:hover {
            background: #ffa726;
        }

        .error-message {
            color: #ff5252;
            margin-top: 15px;
            font-weight: bold;
        }

        /* Bookshop icon/logo */
        .logo {
            font-size: 50px;
            margin-bottom: 15px;
            color: #ffb74d;
        }

        /* Small footer */
        .footer {
            font-size: 12px;
            color: #888;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="login-container">
    <div class="logo">📚</div>
    <h2>Login to Bookshop</h2>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <input type="text" name="username" placeholder="Username" required />
        <input type="password" name="password" placeholder="Password" required />
        <button type="submit">Login</button>
    </form>

    <c:if test="${not empty error}">
        <p class="error-message">${error}</p>
    </c:if>

    <div class="footer">Welcome to Pahana Bookshop! Enjoy your reading journey 📖</div>
</div>
</body>
</html>
