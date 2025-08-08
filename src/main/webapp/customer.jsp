<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>👤 Customer Management</title>
</head>
<body>
<h2>👥 Customer List</h2>

<!-- 🔺 Show error if exists -->
<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>

<!-- ➕ Add Customer Form -->
<form method="post" action="${pageContext.request.contextPath}/customers">
    <label>Account No:</label>
    <input type="text" name="accountNumber" required />

    <label>Name:</label>
    <input type="text" name="name" required />

    <label>Address:</label>
    <input type="text" name="address" required />

    <label>Phone:</label>
    <input type="text" name="phone" required />

    <label>Units Consumed:</label>
    <input type="number" name="unitsConsumed" min="0" required />

    <button type="submit">Add Customer</button>
</form>

<br/>

<!-- 📋 Customer Table -->
<table border="1" cellpadding="5">
    <tr>
        <th>Account No</th>
        <th>Name</th>
        <th>Address</th>
        <th>Phone</th>
        <th>Units</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="c" items="${customers}">
        <tr>
            <td>${c.accountNumber}</td>
            <td>${c.name}</td>
            <td>${c.address}</td>
            <td>${c.phone}</td>
            <td>${c.unitsConsumed}</td>
            <td>
                <a href="${pageContext.request.contextPath}/customers/edit?accountNumber=${c.accountNumber}">Edit</a> |
                <a href="${pageContext.request.contextPath}/customers/delete?accountNumber=${c.accountNumber}"
                   onclick="return confirm('Delete this customer?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
