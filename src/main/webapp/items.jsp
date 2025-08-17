<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="cpath" value="${pageContext.request.contextPath}" />

<html>
<head>
    <title>📦 Items - Pahana Bookshop</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #0f0f0f, #1a1a1a);
            color: #fff;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 1000px;
            margin: 40px auto;
            padding: 30px 25px;
            background: rgba(30, 30, 30, 0.95);
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.7);
            backdrop-filter: blur(6px);
        }

        h2 {
            text-align: center;
            color: #ffb74d;
            margin-bottom: 25px;
            font-size: 32px;
            letter-spacing: 1px;
        }

        h3 {
            color: #ffb74d;
            margin-bottom: 15px;
        }

        .error, .success {
            padding: 12px 15px;
            border-radius: 10px;
            margin-bottom: 15px;
            font-weight: bold;
        }

        .error { background: #b71c1c; color: #fff; }
        .success { background: #1b5e20; color: #fff; }

        form.inline-form, form.inline-form .field {
            display: inline-block;
            margin-right: 15px;
        }

        label {
            margin-right: 6px;
            font-weight: 600;
            color: #ddd;
        }

        input[type=text], input[type=number], select {
            padding: 8px 10px;
            border-radius: 6px;
            border: none;
            background: #444;
            color: #fff;
            outline: none;
        }

        input[type=text]:focus, input[type=number]:focus, select:focus {
            border: 1px solid #ffb74d;
            box-shadow: 0 0 6px rgba(255,183,77,0.6);
        }

        button, .btn {
            padding: 8px 14px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: bold;
            border: none;
            transition: all 0.3s ease;
            background: #ffb74d;
            color: #1a1a1a;
        }

        button:hover, .btn:hover {
            background: #ffa726;
            box-shadow: 0 5px 15px rgba(255,183,77,0.5);
            transform: translateY(-2px);
        }

        /* Flexbox items container */
        .items-grid {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            margin-top: 20px;
        }

        .item-card {
            background: #2c2c2c;
            flex: 1 1 220px;
            padding: 20px;
            border-radius: 12px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            transition: all 0.3s ease;
        }

        .item-card:hover {
            transform: translateY(-5px) scale(1.03);
            box-shadow: 0 12px 25px rgba(255,183,77,0.5);
            background-color: #333333;
        }

        .item-card h4 {
            margin: 0 0 10px;
            color: #ffb74d;
        }

        .item-card p {
            margin: 4px 0;
            color: #ddd;
        }

        .card-actions {
            display: flex;
            justify-content: space-between;
            margin-top: 12px;
        }

        .muted {
            color: #aaa;
            text-align: center;
            padding: 15px 0;
            width: 100%;
        }

        .edit-card {
            display: none;
            flex-direction: column;
            gap: 10px;
            margin-top: 10px;
            padding-top: 10px;
            border-top: 1px solid #444;
        }

    </style>
</head>
<body>
<div class="container">
    <h2>📦 Items Management</h2>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="success">${success}</div>
    </c:if>

    <!-- Add new item -->
    <h3>➕ Add New Item</h3>
    <form action="${cpath}/items" method="post">
        <input type="hidden" name="action" value="add">
        <div class="inline-form">
            <span class="field">
                <label>Item ID:</label>
                <input type="text" name="itemId" required>
            </span>
            <span class="field">
                <label>Name:</label>
                <input type="text" name="name" required>
            </span>
            <span class="field">
                <label>Price:</label>
                <input type="number" name="price" step="0.01" min="0" required>
            </span>
            <button type="submit">Save</button>
        </div>
    </form>

    <!-- Items Grid -->
    <h3>📋 All Items</h3>
    <div class="items-grid">
        <c:choose>
            <c:when test="${empty items}">
                <div class="muted">No items found.</div>
            </c:when>
            <c:otherwise>
                <c:forEach var="it" items="${items}">
                    <div class="item-card">
                        <div>
                            <h4>${it.name}</h4>
                            <p><strong>ID:</strong> ${it.itemId}</p>
                            <p><strong>Price:</strong> LKR <fmt:formatNumber value="${it.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/></p>
                        </div>
                        <div class="card-actions">
                            <button type="button" onclick="toggleEdit(this)">Edit</button>
                            <form action="${cpath}/items" method="post" onsubmit="return confirm('Delete item ${it.itemId}?');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="itemId" value="${it.itemId}">
                                <input type="hidden" name="price" value="${it.price}">
                                <input type="hidden" name="name" value="${it.name}">
                                <button type="submit">Delete</button>
                            </form>
                        </div>
                        <!-- Inline edit -->
                        <div class="edit-card">
                            <form action="${cpath}/items" method="post" class="inline-form">
                                <input type="hidden" name="action" value="edit">
                                <input type="hidden" name="itemId" value="${it.itemId}">
                                <input type="text" name="name" value="${it.name}" placeholder="Name" required>
                                <input type="number" name="price" value="${it.price}" step="0.01" min="0" required>
                                <div class="card-actions">
                                    <button type="submit">Save</button>
                                    <button type="button" onclick="toggleEdit(this)">Cancel</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    function toggleEdit(btn) {
        const card = btn.closest('.item-card');
        const editCard = card.querySelector('.edit-card');
        if(editCard) editCard.style.display = (editCard.style.display === 'flex') ? 'none' : 'flex';
    }
</script>
</body>
</html>
