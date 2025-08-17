<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="cpath" value="${pageContext.request.contextPath}" />

<html>
<head>
    <title>📋 Manage Customers - Pahana Bookshop</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #121212; color: #fff; margin: 0; padding: 0; }

        .container {
            max-width: 1000px;
            margin: 50px auto;
            padding: 40px 30px;
            background-color: #1e1e1e;
            border-radius: 12px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.5);
        }

        h2 {
            text-align: center;
            color: #ffb74d;
            font-size: 32px;
            margin-bottom: 30px;
        }

        .error {
            color: #f06292;
            text-align: center;
            margin-bottom: 20px;
        }

        /* Add customer form inline */
        .add-customer-form {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            align-items: center;
            margin-bottom: 30px;
        }

        .add-customer-form input {
            padding: 8px;
            border-radius: 6px;
            border: none;
            outline: none;
            min-width: 120px;
        }

        .add-customer-form button {
            background-color: #ffb74d;
            color: #121212;
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            font-weight: bold;
            cursor: pointer;
            transition: 0.3s;
        }

        .add-customer-form button:hover {
            background-color: #ffa726;
        }

        hr {
            border: 1px solid #333333;
            margin: 30px 0;
        }

        /* Card-style customers grid */
        .customers-grid {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
        }

        .customer-card {
            background-color: #2c2c2c;
            padding: 20px;
            border-radius: 12px;
            flex: 1 1 calc(33% - 20px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.4);
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            transition: all 0.3s ease;
        }

        .customer-card:hover {
            transform: translateY(-5px) scale(1.02);
            box-shadow: 0 8px 20px rgba(255,183,77,0.5);
        }

        .customer-card h3 {
            margin: 0 0 10px 0;
            color: #ffb74d;
        }

        .customer-card p {
            margin: 5px 0;
        }

        .actions {
            margin-top: 15px;
        }

        .actions a {
            color: #ffb74d;
            text-decoration: none;
            font-weight: bold;
            margin-right: 10px;
            transition: 0.3s;
            cursor: pointer;
        }

        .actions a:hover {
            color: #ffa726;
        }

        .back-link {
            display: inline-block;
            margin-top: 30px;
            color: #ffb74d;
            text-decoration: none;
            font-weight: bold;
            transition: 0.3s;
        }

        .back-link:hover {
            color: #ffa726;
        }

        /* Responsive adjustments */
        @media (max-width: 800px) {
            .customer-card { flex: 1 1 calc(50% - 20px); }
        }

        @media (max-width: 500px) {
            .customer-card { flex: 1 1 100%; }
            .add-customer-form { flex-direction: column; gap: 10px; }
        }

        /* Inline edit form inside card (keeps existing style) */
        .edit-form { display: none; margin-top: 12px; border-top: 1px solid #3a3a3a; padding-top: 12px; }
        .edit-form input { padding: 8px; border-radius: 6px; border: none; outline: none; margin-right: 8px; margin-bottom: 8px; }
        .edit-form button {
            background-color: #ffb74d;
            color: #121212;
            padding: 8px 14px;
            border: none;
            border-radius: 6px;
            font-weight: bold;
            cursor: pointer;
            transition: 0.3s;
            margin-right: 8px;
        }
        .edit-form button:hover { background-color: #ffa726; }
    </style>
</head>
<body>
<div class="container">
    <h2>📋 Manage Customers</h2>

    <!-- Show messages -->
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>
    <c:if test="${not empty success}">
        <p class="error">${success}</p>
    </c:if>

    <!-- Add Customer Form: inline -->
    <form class="add-customer-form" action="${cpath}/customers" method="post">
        <input type="text" name="accountNumber" placeholder="Account Number" required>
        <input type="text" name="name" placeholder="Name" required>
        <input type="text" name="address" placeholder="Address" required>
        <input type="text" name="phone" placeholder="Phone" required>
        <input type="number" name="unitsConsumed" placeholder="Units Consumed" required>
        <!-- No 'action' means servlet treats as Add -->
        <button type="submit">➕ Add Customer</button>
    </form>

    <hr>

    <!-- Customers in card-style grid -->
    <div class="customers-grid">
        <c:forEach var="customer" items="${customers}" varStatus="s">
            <div class="customer-card" id="card-${s.index}">
                <h3>${customer.name}</h3>
                <p><strong>Account Number:</strong> ${customer.accountNumber}</p>
                <p><strong>Address:</strong> ${customer.address}</p>
                <p><strong>Phone:</strong> ${customer.phone}</p>
                <p><strong>Units Consumed:</strong> ${customer.unitsConsumed}</p>

                <div class="actions">
                    <a href="#" onclick="openEdit(${s.index}); return false;">✏️ Edit</a>
                    <a href="#" onclick="return deleteCustomer(${s.index});">🗑️ Delete</a>
                </div>

                <!-- Inline Edit Form (posts to /customers with action=edit) -->
                <div class="edit-form" id="edit-form-${s.index}">
                    <form action="${cpath}/customers" method="post">
                        <input type="hidden" name="action" value="edit">
                        <input type="text" name="accountNumber" value="${customer.accountNumber}" readonly>
                        <input type="text" name="name" value="${customer.name}" placeholder="Name" required>
                        <input type="text" name="address" value="${customer.address}" placeholder="Address" required>
                        <input type="text" name="phone" value="${customer.phone}" placeholder="Phone" required>
                        <input type="number" name="unitsConsumed" value="${customer.unitsConsumed}" placeholder="Units Consumed" min="0" required>
                        <button type="submit">💾 Save</button>
                        <button type="button" onclick="closeEdit(${s.index})">✖ Cancel</button>
                    </form>
                </div>

                <!-- Hidden Delete Form to satisfy servlet's parameter parsing -->
                <form id="del-form-${s.index}" action="${cpath}/customers" method="post" style="display:none;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="accountNumber" value="${customer.accountNumber}">
                    <input type="hidden" name="name" value="${customer.name}">
                    <input type="hidden" name="address" value="${customer.address}">
                    <input type="hidden" name="phone" value="${customer.phone}">
                    <input type="hidden" name="unitsConsumed" value="${customer.unitsConsumed}">
                </form>
            </div>
        </c:forEach>
    </div>

    <a class="back-link" href="${cpath}/dashboard.jsp">🏠 Back to Dashboard</a>
</div>

<script>
    function openEdit(idx) {
        var form = document.getElementById('edit-form-' + idx);
        if (form) form.style.display = 'block';
    }
    function closeEdit(idx) {
        var form = document.getElementById('edit-form-' + idx);
        if (form) form.style.display = 'none';
    }
    function deleteCustomer(idx) {
        if (!confirm('Are you sure you want to delete this customer?')) return false;
        var f = document.getElementById('del-form-' + idx);
        if (f) f.submit();
        return false;
    }
</script>
</body>
</html>