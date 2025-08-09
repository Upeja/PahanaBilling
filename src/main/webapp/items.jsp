<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>📦 Manage Items</title>
</head>
<body>
<h2>📦 Items</h2>

<!-- Show error if exists -->
<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>

<!-- Add Item Form -->
<form action="${pageContext.request.contextPath}/items" method="post">
    <label>Item ID:</label> <input type="text" name="itemId" required><br>
    <label>Name:</label> <input type="text" name="name" required><br>
    <label>Price (LKR):</label> <input type="number" step="0.01" name="price" required><br>
    <button type="submit">Add Item</button>
</form>

<hr>

<!-- Items Table -->
<table border="1" cellpadding="5">
    <tr>
        <th>Item ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="item" items="${items}">
        <tr>
            <td>${item.itemId}</td>
            <td>${item.name}</td>
            <td>${item.price}</td>
            <td>
                <a href="${pageContext.request.contextPath}/items/edit?itemId=${item.itemId}">Edit</a>
                <a href="${pageContext.request.contextPath}/items/delete?itemId=${item.itemId}"
                   onclick="return confirm('Delete this item?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>

<br>
<a href="${pageContext.request.contextPath}/dashboard.jsp">🏠 Back to Dashboard</a>

</body>
</html>
