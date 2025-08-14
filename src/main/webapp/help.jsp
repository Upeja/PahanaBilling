<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Help & User Guide</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #121212;
            color: #e0e0e0;
            padding: 40px;
            line-height: 1.6;
        }

        h1 {
            color: #ffb74d;
            text-align: center;
            font-size: 36px;
            margin-bottom: 30px;
        }

        h2 {
            color: #ffa726;
            margin-top: 0;
        }

        .help-section {
            background: #1e1e1e;
            border-radius: 12px;
            padding: 20px 25px;
            margin-bottom: 20px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.4);
            transition: all 0.3s ease;
        }

        .help-section:hover {
            transform: translateY(-4px) scale(1.01);
            box-shadow: 0 6px 20px rgba(255,183,77,0.3);
        }

        ul {
            margin: 0;
            padding-left: 20px;
        }

        ul li {
            margin-bottom: 6px;
        }

        a.back-link {
            display: inline-block;
            margin-top: 30px;
            text-decoration: none;
            background: #ffb74d;
            color: #121212;
            padding: 10px 18px;
            border-radius: 8px;
            font-weight: bold;
            transition: all 0.3s ease;
        }

        a.back-link:hover {
            background: #ffa726;
        }

        /* Make emoji titles pop */
        .help-section h2 {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        /* Responsive adjustments */
        @media (max-width: 600px) {
            body {
                padding: 20px;
            }
            .help-section {
                padding: 15px 18px;
            }
            h1 {
                font-size: 28px;
            }
        }
    </style>
</head>
<body>

<h1>Help & User Guide</h1>

<div class="help-section">
    <h2>📋 Overview</h2>
    <p>This system allows you to manage <b>Customers</b>, <b>Items</b>, and <b>Bills</b> easily. Follow the guide below for each section.</p>
</div>

<div class="help-section">
    <h2>👤 Managing Customers</h2>
    <ul>
        <li>Go to <b>Manage Customers</b> from the dashboard.</li>
        <li>Fill in <b>Account Number</b>, <b>Name</b>, and <b>Email</b>.</li>
        <li>Click <b>Add</b> to save the customer.</li>
        <li>Use <b>Edit</b> or <b>Delete</b> buttons to update/remove customers.</li>
    </ul>
</div>

<div class="help-section">
    <h2>📦 Managing Items</h2>
    <ul>
        <li>Go to <b>Manage Items</b> from the dashboard.</li>
        <li>Fill in <b>Item ID</b>, <b>Name</b>, and <b>Price</b>.</li>
        <li>Click <b>Add</b> to save the item.</li>
        <li>Use <b>Edit</b> or <b>Delete</b> to update/remove items.</li>
    </ul>
</div>

<div class="help-section">
    <h2>🧾 Generating Bills</h2>
    <ul>
        <li>Go to <b>Generate Bill</b> from the dashboard.</li>
        <li>Select the <b>Customer</b> and <b>Item</b> from the dropdowns.</li>
        <li>Enter <b>Units</b> and the <b>Unit Price</b> will auto-fill.</li>
        <li>Click <b>Generate Bill</b> to save it.</li>
        <li>After generating, you can download the bill PDF.</li>
    </ul>
</div>

<div class="help-section">
    <h2>💡 Tips</h2>
    <ul>
        <li>Make sure the database is running before using the system.</li>
        <li>Use clear and unique IDs for Customers and Items.</li>
        <li>Check the PDF folder to confirm bill exports.</li>
    </ul>
</div>

<a href="dashboard.jsp" class="back-link">⬅ Back to Dashboard</a>

</body>
</html>
