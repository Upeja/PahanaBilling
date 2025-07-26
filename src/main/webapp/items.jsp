<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Items</title></head>
<body>
<h2>Add Item</h2>
<form method="post" action="items">
    ID: <input type="text" name="itemId" required><br>
    Name: <input type="text" name="name" required><br>
    Price: <input type="number" step="0.01" name="price" required><br>
    <button type="submit">Add Item</button>
</form>

<h2>All Items</h2>
<table border="1">
    <tr><th>ID</th><th>Name</th><th>Price</th></tr>
    <c:forEach var="item" items="${items}">
        <tr>
            <td>${item.itemId}</td>
            <td>${item.name}</td>
            <td>${item.price}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
