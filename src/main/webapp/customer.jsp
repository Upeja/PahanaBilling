<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>📋 Manage Customers - Pahana Bookshop</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #121212;
            color: #fff;
            margin: 0;
            padding: 0;
        }

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
            .customer-card {
                flex: 1 1 calc(50% - 20px);
            }
        }

        @media (max-width: 500px) {
            .customer-card {
                flex: 1 1 100%;
            }

            .add-customer-form {
                flex-direction: column;
                gap: 10px;
            }
        }

    </style>
</head>
<body>

<div class="container">
    <h2>📋 Manage Customers</h2>

    <!-- Show error if exists -->
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <!-- Add Customer Form: inline -->
    <form class="add-customer-form" action="${pageContext.request.contextPath}/customers" method="post">
        <input type="text" name="accountNumber" placeholder="Account Number" required>
        <input type="text" name="name" placeholder="Name" required>
        <input type="text" name="address" placeholder="Address" required>
        <input type="text" name="phone" placeholder="Phone" required>
        <input type="number" name="unitsConsumed" placeholder="Units Consumed" required>
        <button type="submit">➕ Add Customer</button>
    </form>

    <hr>

    <!-- Customers in card-style grid -->
    <div class="customers-grid">
        <c:forEach var="customer" items="${customers}">
            <div class="customer-card">
                <h3>${customer.name}</h3>
                <p><strong>Account Number:</strong> ${customer.accountNumber}</p>
                <p><strong>Address:</strong> ${customer.address}</p>
                <p><strong>Phone:</strong> ${customer.phone}</p>
                <p><strong>Units Consumed:</strong> ${customer.unitsConsumed}</p>
                <div class="actions">
                    <a href="${pageContext.request.contextPath}/customers/edit?accountNumber=${customer.accountNumber}">✏️ Edit</a>
                    <a href="${pageContext.request.contextPath}/customers/delete?accountNumber=${customer.accountNumber}" onclick="return confirm('Are you sure?')">🗑️ Delete</a>
                </div>
            </div>
        </c:forEach>
    </div>

    <a class="back-link" href="${pageContext.request.contextPath}/dashboard.jsp">🏠 Back to Dashboard</a>
</div>

</body>
</html>
