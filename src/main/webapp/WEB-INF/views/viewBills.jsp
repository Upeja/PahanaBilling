<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>📋 Bill History</title>
</head>
<body>
<h2>📋 Previous Bills</h2>

<!-- ✅ Table of Bills -->
<table border="1" cellpadding="5">
    <tr>
        <th>Bill ID</th>
        <th>Customer ID</th>
        <th>Item ID</th>
        <th>Units</th>
        <th>Total Amount (LKR)</th>
        <th>Date</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="bill" items="${bills}">
        <tr>
            <td>${bill.billId}</td>
            <td>${bill.customerId}</td>
            <td>${bill.itemId}</td>
            <td>${bill.units}</td>
            <td>${bill.totalAmount}</td>
            <td>${bill.dateTime}</td>
            <td>
                <a href="${pageContext.request.contextPath}/bills/pdf?billId=${bill.billId}" target="_blank">🧾 Download PDF</a>
            </td>
        </tr>
    </c:forEach>
</table>

<br/>
<a href="${pageContext.request.contextPath}/bill">➕ Create New Bill</a>
</body>
</html>
