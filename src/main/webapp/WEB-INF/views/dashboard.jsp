<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Dashboard</title></head>
<body>
<h2>📊 Pahana Billing Dashboard</h2>
<ul>
  <li><a href="${pageContext.request.contextPath}/items">Manage Items</a></li>
  <li><a href="${pageContext.request.contextPath}/customer.jsp">Register Customers</a></li>
  <li><a href="${pageContext.request.contextPath}/bills">Generate Bill</a></li>
  <li><a href="${pageContext.request.contextPath}/bills/view">View All Bills</a></li>
</ul>
</body>
</html>
