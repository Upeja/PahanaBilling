<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>📊 Pahana Billing Dashboard</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      padding: 50px;
      background-color: #f4f4f4;
    }
    .container {
      max-width: 500px;
      margin: auto;
      background: white;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }
    h2 {
      text-align: center;
      margin-bottom: 20px;
      color: #333;
    }
    ul {
      list-style: none;
      padding: 0;
    }
    li {
      margin: 15px 0;
    }
    a {
      text-decoration: none;
      font-size: 16px;
      color: #3366cc;
    }
    a:hover {
      color: #003366;
    }
  </style>
</head>
<body>

<div class="container">
  <h2>📊 Pahana Billing Dashboard</h2>
  <ul>
    <li>📦 <a href="${pageContext.request.contextPath}/items">Manage Items</a></li>
    <li>👤 <a href="${pageContext.request.contextPath}/customer.jsp">Register Customers</a></li>
    <li>🧾 <a href="${pageContext.request.contextPath}/bill">Generate Bill</a></li>
    <li>📋 <a href="${pageContext.request.contextPath}/viewBills.jsp">View All Bills</a></li>
    <li>🚪 <a href="#">Logout</a></li>
    <li>❓ <a href="#">Help</a></li>
  </ul>
</div>

</body>
</html>
