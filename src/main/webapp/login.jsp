<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>🔐 Login - Pahana Billing</title>
</head>
<body>
<h2>🔐 Login</h2>

<form method="post" action="${pageContext.request.contextPath}/login">
    Username: <input type="text" name="username" required /><br><br>
    Password: <input type="password" name="password" required /><br><br>
    <button type="submit">Login</button>
</form>

<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>
</body>
</html>
