<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>📦 Manage Items - Pahana Bookshop</title>
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

        /* Add item form in one line */
        .add-item-form {
            display: flex;
            gap: 15px;
            align-items: center;
            margin-bottom: 30px;
            flex-wrap: wrap;
        }

        .add-item-form input {
            padding: 8px;
            border-radius: 6px;
            border: none;
            outline: none;
            min-width: 120px;
        }

        .add-item-form button {
            background-color: #ffb74d;
            color: #121212;
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            font-weight: bold;
            cursor: pointer;
            transition: 0.3s;
        }

        .add-item-form button:hover {
            background-color: #ffa726;
        }

        hr {
            border: 1px solid #333333;
            margin: 30px 0;
        }

        /* Card-style items */
        .items-grid {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
        }

        .item-card {
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

        .item-card:hover {
            transform: translateY(-5px) scale(1.02);
            box-shadow: 0 8px 20px rgba(255,183,77,0.5);
        }

        .item-card h3 {
            margin: 0 0 10px 0;
            color: #ffb74d;
        }

        .item-card p {
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
            .item-card {
                flex: 1 1 calc(50% - 20px);
            }
        }

        @media (max-width: 500px) {
            .item-card {
                flex: 1 1 100%;
            }

            .add-item-form {
                flex-direction: column;
                gap: 10px;
            }
        }

    </style>
</head>
<body>

<div class="container">
    <h2>📦 Manage Items</h2>

    <!-- Show error if exists -->
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <!-- Add Item Form: inline with button -->
    <form class="add-item-form" action="${pageContext.request.contextPath}/items" method="post">
        <input type="text" name="itemId" placeholder="Item ID" required>
        <input type="text" name="name" placeholder="Name" required>
        <input type="number" step="0.01" name="price" placeholder="Price (LKR)" required>
        <button type="submit">➕ Add Item</button>
    </form>

    <hr>

    <!-- Items in card-style grid -->
    <div class="items-grid">
        <c:forEach var="item" items="${items}">
            <div class="item-card">
                <h3>${item.name}</h3>
                <p><strong>Item ID:</strong> ${item.itemId}</p>
                <p><strong>Price:</strong> ${item.price} LKR</p>
                <div class="actions">
                    <a href="${pageContext.request.contextPath}/items/edit?itemId=${item.itemId}">✏️ Edit</a>
                    <a href="${pageContext.request.contextPath}/items/delete?itemId=${item.itemId}" onclick="return confirm('Delete this item?')">🗑️ Delete</a>
                </div>
            </div>
        </c:forEach>
    </div>

    <a class="back-link" href="${pageContext.request.contextPath}/dashboard.jsp">🏠 Back to Dashboard</a>
</div>

</body>
</html>
