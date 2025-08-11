<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<html>
<head>
    <title>Generate Bill</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }
        th { background-color: #f2f2f2; }
        .success { color: green; }
        .error { color: red; }
        .btn { padding: 6px 12px; border: none; cursor: pointer; }
        .btn-green { background-color: green; color: white; }
        .btn-red { background-color: red; color: white; }
    </style>
</head>
<body>

<h2>Generate New Bill</h2>

<div>
    <form id="billForm">
        <label>Select Customer:</label>
        <select name="customerId" required>
            <option value="">-- Select Customer --</option>
            <c:forEach var="customer" items="${customers}">
                <option value="${customer.accountNumber}">${customer.name}</option>
            </c:forEach>
        </select>
        <br><br>

        <label>Select Item:</label>
        <select name="itemId" id="itemId" required>
            <option value="">-- Select Item --</option>
            <c:forEach var="item" items="${items}">
                <option value="${item.itemId}" data-price="${item.price}">${item.name}</option>
            </c:forEach>
        </select>
        <br><br>

        <label>Units:</label>
        <input type="number" name="units" id="units" min="1" required>
        <br><br>

        <label>Unit Price (Rs.):</label>
        <input type="text" name="unitPrice" id="unitPrice" readonly>
        <br><br>

        <label>Total Amount (Rs.):</label>
        <input type="text" id="totalAmount" readonly>
        <br><br>

        <button type="submit" class="btn btn-green">Generate Bill</button>
    </form>

    <p id="message"></p>
</div>

<h2>All Bills</h2>
<table id="billTable">
    <thead>
    <tr>
        <th>Bill ID</th>
        <th>Customer ID</th>
        <th>Item ID</th>
        <th>Units</th>
        <th>Unit Price (Rs.)</th>
        <th>Total Amount (Rs.)</th>
        <th>Date & Time</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="bill" items="${bills}">
        <tr>
            <td>${bill.billId}</td>
            <td>${bill.customerId}</td>
            <td>${bill.itemId}</td>
            <td>${bill.units}</td>
            <td><fmt:formatNumber value="${bill.unitPrice}" type="number" minFractionDigits="2"/></td>
            <td><fmt:formatNumber value="${bill.totalAmount}" type="number" minFractionDigits="2"/></td>
            <td>${bill.dateTime}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<script>
    // Auto-fill price & calculate total
    document.getElementById("itemId").addEventListener("change", function() {
        const selected = this.options[this.selectedIndex];
        const price = selected.getAttribute("data-price");
        document.getElementById("unitPrice").value = price || "";
        calculateTotal();
    });

    document.getElementById("units").addEventListener("input", calculateTotal);

    function calculateTotal() {
        let units = document.getElementById("units").value;
        let price = document.getElementById("unitPrice").value;
        document.getElementById("totalAmount").value = (units && price) ? (units * price).toFixed(2) : "";
    }

    // AJAX form submit
    document.getElementById("billForm").addEventListener("submit", function(e) {
        e.preventDefault();
        const formData = new FormData(this);

        fetch("${pageContext.request.contextPath}/bill", {
            method: "POST",
            body: formData
        })
            .then(response => response.text())
            .then(html => {
                document.open();
                document.write(html);
                document.close();
            })
            .catch(err => {
                document.getElementById("message").innerHTML = "<span class='error'>Error generating bill.</span>";
            });
    });
</script>

</body>
</html>
