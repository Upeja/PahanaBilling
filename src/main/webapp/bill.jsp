<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>🧾 Generate Bill</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #0f0f0f, #1a1a1a);
            color: #fff;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 900px;
            margin: 40px auto;
            padding: 30px 25px;
            background: rgba(30, 30, 30, 0.95);
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.7);
            backdrop-filter: blur(6px);
            animation: fadeIn 0.8s ease-in-out;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        h2 {
            text-align: center;
            color: #ffb74d;
            margin-bottom: 30px;
            font-size: 30px;
            letter-spacing: 1px;
        }

        h3, h4 {
            color: #ffb74d;
            margin-bottom: 15px;
        }

        .form-section, .items-section {
            border: 1px solid #444;
            background: #2c2c2c;
            padding: 20px;
            border-radius: 12px;
            margin-bottom: 25px;
            box-shadow: 0 6px 15px rgba(0,0,0,0.6);
        }

        label {
            display: inline-block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #ddd;
        }

        select, input[type="number"] {
            padding: 8px 10px;
            border: none;
            border-radius: 6px;
            margin-right: 10px;
            background: #444;
            color: #fff;
            outline: none;
        }

        select:focus, input[type="number"]:focus {
            border: 1px solid #ffb74d;
            box-shadow: 0 0 6px rgba(255,183,77,0.6);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
            border-radius: 10px;
            overflow: hidden;
        }

        th, td {
            padding: 12px 10px;
            text-align: left;
        }

        th {
            background: #ffb74d;
            color: #1a1a1a;
            font-weight: bold;
        }

        td {
            background: #2c2c2c;
            border-bottom: 1px solid #444;
        }

        tr:hover td {
            background: #3a3a3a;
        }

        button {
            padding: 10px 18px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: bold;
            transition: all 0.3s ease;
            background: #ffb74d;
            color: #1a1a1a;
        }

        button:hover {
            background: #ffa726;
            box-shadow: 0 5px 15px rgba(255,183,77,0.5);
            transform: translateY(-2px);
        }

        #grandTotal {
            font-weight: bold;
            font-size: 1.3em;
            color: #ffb74d;
        }

        .error {
            background: #b71c1c;
            color: #fff;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 15px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>🧾 Generate New Bill</h2>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form id="billForm" action="bill" method="post" target="_blank">
        <div class="form-section">
            <label for="customer">👤 Select Customer:</label>
            <select id="customer" name="customerId" required>
                <c:forEach var="cust" items="${customers}">
                    <option value="${cust.accountNumber}">${cust.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-section">
            <h4>➕ Add Items</h4>
            <label for="item">Item:</label>
            <select id="item">
                <c:forEach var="itm" items="${items}">
                    <option value="${itm.itemId}" data-price="${itm.price}">${itm.name}</option>
                </c:forEach>
            </select>
            <label for="quantity">Qty:</label>
            <input type="number" id="quantity" value="1" min="1">
            <button type="button" onclick="addItem()">Add Item</button>
        </div>

        <h3>📋 Bill Items</h3>
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

        <button type="submit">💾 Generate Bill PDF</button>
    </form>
</div>

<script>

    function addItem() {
        const itemSelect = document.getElementById('item');
        const selectedOption = itemSelect.options[itemSelect.selectedIndex];
        const itemId = selectedOption.value;
        const itemName = selectedOption.text;
        const unitPriceStr = selectedOption.getAttribute('data-price');
        const unitPrice = parseFloat(unitPriceStr || '0');
        const quantity = parseInt(document.getElementById('quantity').value, 10);

        if (isNaN(quantity) || quantity <= 0) {
            alert("Please enter a valid quantity.");
            return;
        }
        if (isNaN(unitPrice)) {
            alert("Selected item has no valid price.");
            return;
        }

        const subtotal = unitPrice * quantity;

        const tableBody = document.querySelector("#billItemsTable tbody");
        const newRow = tableBody.insertRow();

        const key = itemId + "-" + Date.now();
        newRow.setAttribute('data-key', key);

        newRow.innerHTML =
            '<td>' + escapeHtml(itemId) + '</td>' +
            '<td>' + escapeHtml(itemName) + '</td>' +
            '<td>' + quantity + '</td>' +
            '<td>' + unitPrice.toFixed(2) + '</td>' +
            '<td>' + subtotal.toFixed(2) + '</td>' +
            '<td><button type="button" onclick="removeItem(this)">Remove</button></td>';

        const hiddenItemsDiv = document.getElementById('hidden-items');
        const group = document.createElement('div');
        group.className = 'hidden-group';
        group.setAttribute('data-key', key);
        group.innerHTML =
            '<input type="hidden" name="itemId" value="' + escapeAttr(itemId) + '">' +
            '<input type="hidden" name="quantity" value="' + quantity + '">' +
            '<input type="hidden" name="unitPrice" value="' + unitPrice + '">';
        hiddenItemsDiv.appendChild(group);

        updateGrandTotal();
    }

    function removeItem(button) {
        const row = button.closest('tr');
        if (!row) return;
        const key = row.getAttribute('data-key');

        row.remove();

        const hiddenGroup = document.querySelector('#hidden-items .hidden-group[data-key="' + key + '"]');
        if (hiddenGroup) hiddenGroup.remove();

        updateGrandTotal();
    }

    function updateGrandTotal() {
        let total = 0;
        const tableRows = document.querySelectorAll("#billItemsTable tbody tr");
        tableRows.forEach(row => {
            const cell = row.cells[4];
            if (!cell) return;
            const val = parseFloat(cell.innerText);
            if (!isNaN(val)) total += val;
        });
        document.getElementById('grandTotal').innerText = total.toFixed(2);
    }

    function escapeHtml(s) {
        return String(s).replace(/[&<>"']/g, function(c) {
            return {'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c];
        });
    }
    function escapeAttr(s) {
        return escapeHtml(s);
    }
</script>
</body>
</html>
