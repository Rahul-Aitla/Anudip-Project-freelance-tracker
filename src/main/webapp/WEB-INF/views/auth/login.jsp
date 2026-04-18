<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Freelancer Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="auth-shell">
    <div class="auth-card">
        <p class="kicker">Freelancer Project Tracker</p>
        <h1 class="auth-title">Welcome Back</h1>
        <p class="auth-sub">Track clients, projects, payments, and deadlines in one place.</p>

        <c:if test="${not empty error}">
            <div class="alert error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="field">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="field" style="margin-top: 10px;">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="actions" style="margin-top: 16px;">
                <button class="btn primary" type="submit">Login</button>
                <a class="btn secondary" href="${pageContext.request.contextPath}/register">Create Account</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
