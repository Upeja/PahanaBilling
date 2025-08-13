<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Generate Bill</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<h2>Generate Bill</h2>

<form id="billForm">
    <label>Customer:</label>
    <select name="customerId" required>
        <c:forEach var="c" items="${customers}">
            <option value="${c.accountNumber}">${c.name}</option>
        </c:forEach>
    </select><br><br>

    <label>Item:</label>
    <select name="itemId" id="itemSelect" required>
        <c:forEach var="i" items="${items}">
            <option value="${i.itemId}" data-price="${i.price}">${i.name}</option>
        </c:forEach>
    </select><br><br>

    <label>Unit Price:</label>
    <input type="number" name="unitPrice" id="unitPrice" step="0.01" readonly><br><br>

    <label>Units:</label>
    <input type="number" name="units" id="units" min="1" required><br><br>

    <label>Total:</label>
    <input type="text" id="totalAmount" readonly><br><br>

    <button type="submit">Generate Bill</button>
</form>

<h3>All Bills</h3>
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


<script>
    $(document).ready(function(){
        // Autofill unit price
        $("#itemSelect").change(function(){
            let price = $(this).find(':selected').data('price');
            $("#unitPrice").val(price);
            calcTotal();
        });

        // Calculate total on units change
        $("#units").on("input", function(){
            calcTotal();
        });

        function calcTotal(){
            let price = parseFloat($("#unitPrice").val()) || 0;
            let units = parseInt($("#units").val()) || 0;
            $("#totalAmount").val(price * units);
        }

        // Handle form submit with AJAX
        $("#billForm").submit(function(e){
            e.preventDefault();
            $.post("bill", $(this).serialize(), function(data){
                if(data.success){
                    alert(data.success);
                    loadBills();
                } else {
                    alert(data.error);
                }
            }, "json");
        });

        // Load bills table without reload
        function loadBills(){
            $.get("bill/generate", function(data){
                let rows = "";
                data.forEach(b => {
                    rows += `<tr>
                    <td>${b.billId}</td>
                    <td>${b.customerId}</td>
                    <td>${b.itemId}</td>
                    <td>${b.units}</td>
                    <td>${b.unitPrice}</td>
                    <td>${b.totalAmount}</td>
                    <td>${b.dateTime}</td>
                </tr>`;
                });
                $("#billTable tbody").html(rows);
            }, "json");
        }
    });
</script>
</body>
</html>
