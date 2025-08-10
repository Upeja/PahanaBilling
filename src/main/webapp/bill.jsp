<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Generate Bill</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        table { border-collapse: collapse; width: 100%; margin-top: 15px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
        th { background-color: #f2f2f2; }
        input[readonly] { background-color: #f9f9f9; }
        .btn { padding: 5px 10px; cursor: pointer; }
        .btn-add { color: green; font-size: 18px; }
        .btn-remove { color: red; font-size: 18px; }
    </style>
</head>
<body>
<h2>🧾 Generate Bill</h2>

<form id="billForm">
    <label>Customer:</label>
    <select name="customerId" required>
        <option value="">-- Select Customer --</option>
        <c:forEach var="customer" items="${customers}">
            <option value="${customer.accountNumber}">${customer.name}</option>
        </c:forEach>
    </select>

    <table id="itemsTable">
        <thead>
        <tr>
            <th>Item</th>
            <th>Unit Price</th>
            <th>Units</th>
            <th>Total</th>
            <th>➕/❌</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>
                <select name="itemId[]" class="itemSelect" required>
                    <option value="">-- Select Item --</option>
                    <c:forEach var="item" items="${items}">
                        <option value="${item.itemId}" data-price="${item.price}">${item.name}</option>
                    </c:forEach>
                </select>
            </td>
            <td><input type="number" name="unitPrice[]" readonly></td>
            <td><input type="number" name="units[]" min="1" value="1"></td>
            <td><input type="number" name="total[]" readonly></td>
            <td><button type="button" class="btn btn-add"><i class="fa fa-plus"></i></button></td>
        </tr>
        </tbody>
        <tfoot>
        <tr>
            <th colspan="3" style="text-align:right">Grand Total:</th>
            <th><input type="number" id="grandTotal" readonly></th>
            <th></th>
        </tr>
        </tfoot>
    </table>

    <br>
    <button type="submit">Generate Bill</button>
</form>

<h3>📋 All Bills</h3>
<table id="billsTable">
    <thead>
    <tr>
        <th>Bill ID</th>
        <th>Customer ID</th>
        <th>Item ID</th>
        <th>Units</th>
        <th>Unit Price</th>
        <th>Total</th>
        <th>Date</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="bill" items="${bills}">
        <tr>
            <td>${bill.billId}</td>
            <td>${bill.customerId}</td>
            <td>${bill.itemId}</td>
            <td>${bill.units}</td>
            <td>${bill.unitPrice}</td>
            <td>${bill.totalAmount}</td>
            <td>${bill.dateTime}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<script>
    // Auto-fill price on item change
    $(document).on("change", ".itemSelect", function () {
        let price = $(this).find(":selected").data("price") || 0;
        let row = $(this).closest("tr");
        row.find("input[name='unitPrice[]']").val(price);
        updateRowTotal(row);
    });

    // Update row total when units change
    $(document).on("input", "input[name='units[]']", function () {
        let row = $(this).closest("tr");
        updateRowTotal(row);
    });

    function updateRowTotal(row) {
        let price = parseFloat(row.find("input[name='unitPrice[]']").val()) || 0;
        let units = parseInt(row.find("input[name='units[]']").val()) || 0;
        let total = price * units;
        row.find("input[name='total[]']").val(total.toFixed(2));
        updateGrandTotal();
    }

    function updateGrandTotal() {
        let total = 0;
        $("input[name='total[]']").each(function () {
            total += parseFloat($(this).val()) || 0;
        });
        $("#grandTotal").val(total.toFixed(2));
    }

    // Add more item row
    $(document).on("click", ".btn-add", function () {
        let newRow = `<tr>
            <td>
                <select name="itemId[]" class="itemSelect" required>
                    <option value="">-- Select Item --</option>
                    ${$("select[name='itemId[]']").first().html()}
                </select>
            </td>
            <td><input type="number" name="unitPrice[]" readonly></td>
            <td><input type="number" name="units[]" min="1" value="1"></td>
            <td><input type="number" name="total[]" readonly></td>
            <td><button type="button" class="btn btn-remove"><i class="fa fa-times"></i></button></td>
        </tr>`;
        $("#itemsTable tbody").append(newRow);
    });

    // Remove item row
    $(document).on("click", ".btn-remove", function () {
        $(this).closest("tr").remove();
        updateGrandTotal();
    });

    // AJAX submit bill
    $("#billForm").submit(function (e) {
        e.preventDefault();
        $.ajax({
            type: "POST",
            url: "bill",
            data: $(this).serialize(),
            success: function (response) {
                let data = JSON.parse(response);
                if (data.success) {
                    window.open(data.pdfUrl, "_blank"); // open PDF
                    $("#billsTable tbody").load(location.href + " #billsTable tbody>*");
                    $("#billForm")[0].reset();
                    $("#grandTotal").val("");
                } else {
                    alert("❌ " + data.message);
                }
            },
            error: function () {
                alert("❌ Error saving bill.");
            }
        });
    });
</script>

</body>
</html>
