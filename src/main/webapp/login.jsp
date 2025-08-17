<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>🔐 Login - Pahana Bookshop</title>
    <style>
        /* Dark gradient background */
        body {
            background: linear-gradient(135deg, #0f0f0f, #1a1a1a);
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
            background: rgba(30, 30, 30, 0.95);
            padding: 40px 30px;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.7);
            width: 370px;
            text-align: center;
            backdrop-filter: blur(6px);
            animation: fadeIn 0.8s ease-in-out;
        }

        /* Animation */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(30px); }
            to { opacity: 1; transform: translateY(0); }
        }

        h2 {
            color: #ffb74d;
            margin-bottom: 25px;
            font-size: 26px;
            letter-spacing: 1px;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 14px 15px;
            margin: 12px 0;
            border-radius: 8px;
            border: 1px solid #333;
            background: #2c2c2c;
            color: #fff;
            font-size: 15px;
            outline: none;
            transition: border 0.3s, background 0.3s;
        }

        input:focus {
            border: 1px solid #ffb74d;
            background: #353535;
        }

        input::placeholder {
            color: #aaa;
        }

        button {
            background: linear-gradient(135deg, #ffb74d, #ffa726);
            color: #121212;
            border: none;
            padding: 14px 20px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: bold;
            width: 100%;
            margin-top: 15px;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            font-size: 16px;
        }

        button:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba(255,183,77,0.4);
        }

        .error-message {
            color: #ff5252;
            margin-top: 15px;
            font-weight: bold;
            font-size: 14px;
            animation: shake 0.3s;
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            20%, 60% { transform: translateX(-6px); }
            40%, 80% { transform: translateX(6px); }
        }

        /* Logo */
        .logo {
            font-size: 55px;
            margin-bottom: 10px;
            color: #ffb74d;
            text-shadow: 0 0 15px rgba(255,183,77,0.6);
        }

        /* Footer */
        .footer {
            font-size: 12px;
            color: #aaa;
            margin-top: 25px;
        }

        .footer span {
            color: #ffb74d;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="login-container">
    <div class="logo">📚</div>
    <h2>Login to Pahana Bookshop</h2>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <input type="text" name="username" placeholder="Username" required />
        <input type="password" name="password" placeholder="Password" required />
        <button type="submit">Login</button>
    </form>

    <c:if test="${not empty error}">
        <p class="error-message">${error}</p>
    </c:if>

    <div class="footer">
        Welcome to <span>Pahana Bookshop</span>! Enjoy your reading journey 📖
    </div>
</div>
</body>
</html>
