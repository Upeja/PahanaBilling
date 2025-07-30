<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Customer</title>
</head>
<body>
<h2>✏️ Edit Customer</h2>

<form method="post" action="${pageContext.request.contextPath}/customers/edit">
    <input type="hidden" name="accountNumber" value="${customer.accountNumber}" />

    <label>Name:</label>
    <input type="text" name="name" value="${customer.name}" required /><br/><br/>

    <label>Address:</label>
    <input type="text" name="address" value="${customer.address}" required /><br/><br/>

    <label>Phone:</label>
    <input type="text" name="phone" value="${customer.phone}" required /><br/><br/>

    <label>Units Consumed:</label>
    <input type="number" name="unitsConsumed" value="${customer.unitsConsumed}" min="0" required /><br/><br/>

    <button type="submit">💾 Update</button>
</form>

<br/>
<a href="${pageContext.request.contextPath}/customers">⬅ Back to Customer List</a>
</body>
</html>
