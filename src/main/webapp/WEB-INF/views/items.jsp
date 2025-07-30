<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>🛒 Item Management</title>
</head>
<body>
<h2>📋 Items</h2>

<!-- 🔍 Search Form -->
<form method="get" action="${pageContext.request.contextPath}/items">
    <input type="text" name="search" placeholder="Search by name" value="${search}" />
    <button type="submit">Search</button>
    <a href="${pageContext.request.contextPath}/items">Clear</a>
</form>

<!-- 🔺 Display error if exists -->
<c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
</c:if>

<!-- ➕ Add Item Form -->
<form method="post" action="${pageContext.request.contextPath}/items">
    <label>ID:</label> <input type="text" name="itemId" required>
    <label>Name:</label> <input type="text" name="name" required>
    <label>Price:</label> <input type="number" step="0.01" name="price" required>
    <button type="submit">Add</button>
</form>

<!-- 📋 Item List Table -->
<br/>
<table border="1" cellpadding="5" cellspacing="0">
    <tr><th>ID</th><th>Name</th><th>Price</th><th>Actions</th></tr>
    <c:forEach var="item" items="${items}">
        <tr>
            <td>${item.itemId}</td>
            <td>${item.name}</td>
            <td>${item.price}</td>
            <td>
                <a href="${pageContext.request.contextPath}/items/edit?itemId=${item.itemId}">✏️ Edit</a> |
                <a href="${pageContext.request.contextPath}/items/delete?itemId=${item.itemId}"
                   onclick="return confirm('Are you sure you want to delete this item?')">🗑️ Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
