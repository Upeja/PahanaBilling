<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Manage Customers</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 8px; border: 1px solid #ccc; text-align: left; }
        th { background-color: #f4f4f4; }
        form { margin-top: 20px; }
        input, button { padding: 6px; margin: 4px; }
        .error { color: red; }
    </style>
</head>
<body>
<h2>Manage Customers</h2>

<!-- Error message -->
<c:if test="${not empty error}">
    <div class="error">${error}</div>
</c:if>

<!-- Add customer form -->
<form action="${pageContext.request.contextPath}/customers" method="post">
    <input type="text" name="accountNumber" placeholder="Account Number" required>
    <input type="text" name="name" placeholder="Name" required>
    <input type="text" name="address" placeholder="Address" required>
    <input type="text" name="phone" placeholder="Phone" required>
    <input type="number" name="unitsConsumed" placeholder="Units Consumed" required>
    <button type="submit">Add Customer</button>
</form>

<!-- Customers table -->
<table>
    <thead>
    <tr>
        <th>Account Number</th>
        <th>Name</th>
        <th>Address</th>
        <th>Phone</th>
        <th>Units Consumed</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="customer" items="${customers}">
        <tr>
            <td>${customer.accountNumber}</td>
            <td>${customer.name}</td>
            <td>${customer.address}</td>
            <td>${customer.phone}</td>
            <td>${customer.unitsConsumed}</td>
            <td>
                <a href="${pageContext.request.contextPath}/customers/edit?accountNumber=${customer.accountNumber}">Edit</a>
                |
                <a href="${pageContext.request.contextPath}/customers/delete?accountNumber=${customer.accountNumber}"
                   onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
