<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Item</title>
</head>
<body>
<h2>✏️ Edit Item</h2>

<form method="post" action="${pageContext.request.contextPath}/items/edit">
    <input type="hidden" name="itemId" value="${item.itemId}" />

    <label>Item Name:</label><br>
    <input type="text" name="name" value="${item.name}" required><br><br>

    <label>Price (Rs.):</label><br>
    <input type="number" name="price" step="0.01" value="${item.price}" required><br><br>

    <button type="submit">💾 Update</button>
</form>

<br>
<a href="${pageContext.request.contextPath}/items">⬅ Back to Items</a>
</body>
</html>
