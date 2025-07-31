<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Generate Bill</title>
</head>
<body>
<h2>🧾 Generate Bill</h2>

<form method="post" action="${pageContext.request.contextPath}/bill">

    <!-- 🔽 Customer Select -->
    <label>Customer:</label>
    <select name="customerId" required>
        <option value="">-- Select Customer --</option>
        <c:forEach var="c" items="${customers}">
            <option value="${c.accountNumber}">${c.accountNumber} - ${c.name}</option>
        </c:forEach>
    </select>
    <br/><br/>

    <!-- 🔽 Item Select -->
    <label>Item:</label>
    <select name="itemId" required>
        <option value="">-- Select Item --</option>
        <c:forEach var="i" items="${items}">
            <option value="${i.itemId}">${i.itemId} - ${i.name}</option>
        </c:forEach>
    </select>
    <br/><br/>

    <!-- 🔢 Units -->
    <label>Units:</label>
    <input type="number" name="units" min="1" required />
    <br/><br/>

    <!-- 💵 Unit Price -->
    <label>Unit Price (LKR):</label>
    <input type="number" name="unitPrice" step="0.01" min="0" required />
    <br/><br/>

    <button type="submit">✅ Generate & Save Bill</button>
</form>

<br/>
<a href="${pageContext.request.contextPath}/bills">📋 View Previous Bills</a>

</body>
</html>
