<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>📊 Pahana Bookshop Dashboard</title>
  <style>
    /* Dark theme background */
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background-color: #121212;
      color: #fff;
      margin: 0;
      padding: 0;
    }

    .container {
      max-width: 1000px;
      margin: 50px auto;
      padding: 40px 30px;
      background-color: #1e1e1e;
      border-radius: 12px;
      box-shadow: 0 8px 20px rgba(0,0,0,0.5);
    }

    .logo {
      font-size: 50px;
      text-align: center;
      margin-bottom: 10px;
      color: #ffb74d;
    }

    .session-info {
      text-align: right;
      font-size: 14px;
      margin-bottom: 25px;
      color: #aaa;
    }

    h2 {
      text-align: center;
      color: #ffb74d;
      margin-bottom: 40px;
      font-size: 32px;
    }

    /* Modern card-style menu in one row */
    .menu {
      display: flex;
      justify-content: space-between;
      flex-wrap: nowrap;
      gap: 20px;
    }

    .menu-card {
      background-color: #2c2c2c;
      padding: 25px 20px;
      border-radius: 12px;
      text-align: center;
      font-size: 18px;
      font-weight: bold;
      transition: all 0.3s ease;
      cursor: pointer;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      flex: 1 1 0;
    }

    .menu-card i, .menu-card span {
      margin: 5px 0;
    }

    .menu-card:hover {
      transform: translateY(-5px) scale(1.03);
      box-shadow: 0 12px 25px rgba(255,183,77,0.5);
      background-color: #333333;
      color: #ffb74d;
    }

    .menu-card a {
      color: inherit;
      text-decoration: none;
      width: 100%;
      display: block;
    }

    .footer {
      text-align: center;
      font-size: 13px;
      color: #888;
      margin-top: 35px;
    }

    /* Icon styling */
    .menu-card i {
      font-size: 28px;
      display: block;
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
        <span>Generate Bill</span>
      </a>
    </div>
    <div class="menu-card">
      <a href="${pageContext.request.contextPath}/viewBills.jsp">
        <i class="fas fa-clipboard-list"></i>
        <span>View All Bills</span>
      </a>
    </div>
    <div class="menu-card">
      <a href="#">
        <i class="fas fa-question-circle"></i>
        <span>Help</span>
      </a>
    </div>
  </div>

  <div class="footer">Welcome to Pahana Bookshop! Enjoy your reading journey 📖</div>
</div>

</body>
</html>
