<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Generate Bill</title>
    <style>
        body { font-family: sans-serif; }
        .container { max-width: 800px; margin: auto; padding: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        .form-section, .items-section { border: 1px solid #ccc; padding: 15px; margin-bottom: 20px; }
        button { padding: 8px 12px; cursor: pointer; }
        #grandTotal { font-weight: bold; font-size: 1.2em; }
    </style>
</head>
<body>
<div class="container">
    <h2>Generate New Bill</h2>

    <form id="billForm" action="bill" method="post" target="_blank">
        <div class="form-section">
            <label for="customer">Select Customer:</label>
            <select id="customer" name="customerId" required>
                <c:forEach var="cust" items="${customers}">
                    <option value="${cust.customerId}">${cust.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-section">
            <h4>Add Items</h4>
            <label for="item">Item:</label>
            <select id="item">
                <c:forEach var="itm" items="${items}">
                    <option value="${itm.itemId}" data-price="${itm.unitPrice}">${itm.name}</option>
                </c:forEach>
            </select>
            <label for="quantity">Quantity:</label>
            <input type="number" id="quantity" value="1" min="1">
            <button type="button" onclick="addItem()">Add Item</button>
        </div>

        <h3>Bill Items</h3>
        <table id="billItemsTable">
            <thead>
            <tr>
                <th>Item ID</th>
                <th>Item Name</th>
                <th>Quantity</th>
                <th>Unit Price</th>
                <th>Subtotal</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>

        <p>Grand Total: <span id="grandTotal">0.00</span></p>

        <div id="hidden-items"></div>

        <button type="submit">Generate Bill PDF</button>
    </form>
</div>

<script>
    function addItem() {
        const itemSelect = document.getElementById('item');
        const selectedOption = itemSelect.options[itemSelect.selectedIndex];
        const itemId = selectedOption.value;
        const itemName = selectedOption.text;
        const unitPrice = parseFloat(selectedOption.getAttribute('data-price'));
        const quantity = parseInt(document.getElementById('quantity').value);

        if (isNaN(quantity) || quantity <= 0) {
            alert("Please enter a valid quantity.");
            return;
        }

        const subtotal = unitPrice * quantity;

        // Add visual row to the table
        const tableBody = document.querySelector("#billItemsTable tbody");
        const newRow = tableBody.insertRow();
        newRow.innerHTML = `
            <td>${itemId}</td>
            <td>${itemName}</td>
            <td>${quantity}</td>
            <td>${unitPrice.toFixed(2)}</td>
            <td>${subtotal.toFixed(2)}</td>
            <td><button type="button" onclick="removeItem(this)">Remove</button></td>
        `;

        // IMPORTANT: Add hidden inputs to the form for submission
        const hiddenItemsDiv = document.getElementById('hidden-items');
        hiddenItemsDiv.innerHTML += `
            <input type="hidden" name="itemId" value="${itemId}">
            <input type="hidden" name="quantity" value="${quantity}">
            <input type="hidden" name="unitPrice" value="${unitPrice}">
        `;

        updateGrandTotal();
    }

    function removeItem(button) {
        const row = button.parentNode.parentNode;
        const itemId = row.cells[0].innerText;

        // Remove the visual row
        row.parentNode.removeChild(row);

        // Remove the corresponding hidden inputs
        const hiddenInputs = document.querySelectorAll(`#hidden-items input[value="${itemId}"]`);
        hiddenInputs.forEach(input => input.parentElement.removeChild(input.nextElementSibling.nextElementSibling)); // remove unit price
        hiddenInputs.forEach(input => input.parentElement.removeChild(input.nextElementSibling)); // remove quantity
        hiddenInputs.forEach(input => input.parentElement.removeChild(input)); // remove item id

        updateGrandTotal();
    }

    function updateGrandTotal() {
        let total = 0;
        const tableRows = document.querySelectorAll("#billItemsTable tbody tr");
        tableRows.forEach(row => {
            total += parseFloat(row.cells[4].innerText);
        });
        document.getElementById('grandTotal').innerText = total.toFixed(2);
    }
</script>
</body>
</html>