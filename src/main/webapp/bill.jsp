<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Generate Bill</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        select, input { width: 250px; padding: 5px; margin-bottom: 10px; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        table, th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
        .success { color: green; }
        .error { color: red; }
    </style>
    <script>
        function updatePrice() {
            let price = document.getElementById("itemId").selectedOptions[0].getAttribute("data-price");
            document.getElementById("unitPrice").value = price ? price : "";
        }

        function calculateTotal() {
            let units = document.getElementById("units").value;
            let price = document.getElementById("unitPrice").value;
            if (units && price) {
                document.getElementById("totalAmount").value = (units * price).toFixed(2);
            } else {
                document.getElementById("totalAmount").value = "";
            }
        }
    </script>
</head>
<body>

<h2>Generate New Bill</h2>

<!-- Success / Error messages -->
<c:if test="${not empty success}">
    <p class="success">${success}</p>
</c:if>
<c:if test="${not empty error}">
    <p class="error">${error}</p>
</c:if>

<form action="bill" method="post" onsubmit="return validateForm()">
    <input type="hidden" name="action" value="generateBill">

    <label>Select Customer:</label><br>
    <select name="customerId" required>
        <option value="">-- Select Customer --</option>
        <c:forEach var="customer" items="${customers}">
            <option value="${customer.accountNumber}">${customer.name}</option>
        </c:forEach>
    </select><br>

    <label>Select Item:</label><br>
    <select name="itemId" id="itemId" onchange="updatePrice()" required>
        <option value="">-- Select Item --</option>
        <c:forEach var="item" items="${items}">
            <option value="${item.itemId}" data-price="${item.unitPrice}">${item.itemName}</option>
        </c:forEach>
    </select><br>

    <label>Units:</label><br>
    <input type="number" name="units" id="units" oninput="calculateTotal()" required><br>

    <label>Unit Price (Rs.):</label><br>
    <input type="number" step="0.01" name="unitPrice" id="unitPrice" oninput="calculateTotal()" required><br>

    <label>Total Amount (Rs.):</label><br>
    <input type="text" id="totalAmount" readonly><br><br>

    <button type="submit">Generate Bill</button>
</form>

<h3>All Bills</h3>
<table>
    <tr>
        <th>Bill ID</th>
        <th>Customer ID</th>
        <th>Item ID</th>
        <th>Units</th>
        <th>Unit Price</th>
        <th>Total</th>
        <th>PDF</th>
    </tr>
    <c:forEach var="bill" items="${bills}">
        <tr>
            <td>${bill.billId}</td>
            <td>${bill.customerId}</td>
            <td>${bill.itemId}</td>
            <td>${bill.units}</td>
            <td>${bill.unitPrice}</td>
            <td>${bill.totalAmount}</td>
            <td>
                <a href="${pageContext.request.contextPath}/WEB-INF/generatedpdfs/Bill_${bill.billId}.pdf" target="_blank">
                    View PDF
                </a>
            </td>
        </tr>
    </c:forEach>
</table>

<script>
    function validateForm() {
        let customer = document.querySelector("[name='customerId']").value;
        let item = document.querySelector("[name='itemId']").value;
        let units = document.getElementById("units").value;
        let price = document.getElementById("unitPrice").value;
        if (!customer || !item || !units || !price) {
            alert("⚠️ Please fill all fields before submitting.");
            return false;
        }
        return true;
    }
</script>

</body>
</html>
