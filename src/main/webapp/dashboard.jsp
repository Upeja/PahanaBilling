<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>📊 Pahana Bookshop Dashboard</title>
  <style>
    /* Dark gradient background */
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: linear-gradient(135deg, #0f0f0f, #1a1a1a);
      color: #fff;
      margin: 0;
      padding: 0;
    }

    .container {
      max-width: 1100px;
      margin: 50px auto;
      padding: 40px 30px;
      background: rgba(30, 30, 30, 0.95);
      border-radius: 16px;
      box-shadow: 0 10px 30px rgba(0,0,0,0.7);
      backdrop-filter: blur(6px);
      animation: fadeIn 0.8s ease-in-out;
    }

    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(20px); }
      to { opacity: 1; transform: translateY(0); }
    }

    .logo {
      font-size: 55px;
      text-align: center;
      margin-bottom: 10px;
      color: #ffb74d;
      text-shadow: 0 0 15px rgba(255,183,77,0.6);
    }

    .session-info {
      text-align: right;
      font-size: 14px;
      margin-bottom: 25px;
      color: #bbb;
    }

    .session-info a {
      color: #ffb74d;
      text-decoration: none;
      font-weight: bold;
    }
    .session-info a:hover {
      text-decoration: underline;
    }

    h2 {
      text-align: center;
      color: #ffb74d;
      margin-bottom: 40px;
      font-size: 32px;
      letter-spacing: 1px;
    }

    /* Responsive grid for menu */
    .menu {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
      gap: 25px;
    }

    .menu-card {
      background: #2c2c2c;
      padding: 35px 20px;
      border-radius: 14px;
      text-align: center;
      font-size: 18px;
      font-weight: bold;
      transition: all 0.3s ease;
      cursor: pointer;
      box-shadow: 0 6px 15px rgba(0,0,0,0.6);
    }

    .menu-card i {
      font-size: 34px;
      margin-bottom: 12px;
      color: #ffb74d;
      transition: color 0.3s;
    }

    .menu-card span {
      display: block;
      margin-top: 5px;
    }

    .menu-card:hover {
      transform: translateY(-6px) scale(1.03);
      box-shadow: 0 12px 25px rgba(255,183,77,0.5);
      background: #333;
      color: #ffb74d;
    }

    .menu-card:hover i {
      color: #ffa726;
    }

    .menu-card a {
      color: inherit;
      text-decoration: none;
      display: block;
    }

    .footer {
      text-align: center;
      font-size: 13px;
      color: #aaa;
      margin-top: 40px;
    }

    .footer span {
      color: #ffb74d;
      font-weight: bold;
    }
  </style>
  <!-- FontAwesome for icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<div class="container">
  <div class="logo">📚</div>

  <div class="session-info">
    Logged in as: <strong>${sessionScope.username}</strong> |
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
  </div>

  <h2>📊 Bookshop Dashboard</h2>

  <div class="menu">
    <div class="menu-card">
      <a href="${pageContext.request.contextPath}/items">
        <i class="fas fa-box"></i>
        <span>Manage Items</span>
      </a>
    </div>
    <div class="menu-card">
      <a href="${pageContext.request.contextPath}/customers">
        <i class="fas fa-user"></i>
        <span>Register Customers</span>
      </a>
    </div>
    <div class="menu-card">
      <a href="${pageContext.request.contextPath}/bill">
        <i class="fas fa-file-invoice"></i>
        <span>Order Items</span>
      </a>
    </div>
    <div class="menu-card">
      <a href="help.jsp">
        <i class="fas fa-question-circle"></i>
        <span>Help</span>
      </a>
    </div>
  </div>

  <div class="footer">
    Welcome to <span>Pahana Bookshop</span>! Enjoy your reading journey 📖
  </div>
</div>

</body>
</html>
