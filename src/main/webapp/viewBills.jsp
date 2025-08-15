<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>All Bills</title>
</head>
<body>

<h2>All Bills</h2>

<c:if test="${not empty success}">
    <p style="color:green;">${success}</p>
</c:if>
<c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
</c:if>

<table border="1" id="billTable">
    <thead>
    <tr>
        <th>Bill ID</th>
        <th>Customer</th>
        <th>Item</th>
        <th>Units</th>
        <th>Unit Price</th>
        <th>Total</th>
        <th>Date</th>
        <th>PDF</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="b" items="${bills}">
        <tr>
            <td>${b.billId}</td>
            <td>${b.customerId}</td>
            <td>${b.itemId}</td>
            <td>${b.units}</td>
            <td>${b.unitPrice}</td>
            <td>${b.totalAmount}</td>
            <td>${b.dateTime}</td>
            <td>
                <form action="bill/pdf" method="get" target="_blank">
                    <input type="hidden" name="billId" value="${b.billId}">
                    <button type="submit">Generate PDF</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
