<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty pageTitle ? 'Freelancer Tracker' : pageTitle}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<header class="topbar">
    <div class="topbar-inner">
        <a class="brand" href="${pageContext.request.contextPath}/dashboard">Freelancer Tracker</a>
        <nav class="nav-links">
            <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/clients">Clients</a>
            <a href="${pageContext.request.contextPath}/projects">Projects</a>
            <a href="${pageContext.request.contextPath}/payments">Payments</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn secondary">Logout</a>
        </nav>
        <span class="user-chip">${sessionScope.fullName}</span>
    </div>
</header>
<main class="page-wrap">
    <div class="page-heading">
        <h1>${empty pageHeading ? 'Freelancer Tracker' : pageHeading}</h1>
        <c:if test="${not empty pageSubheading}">
            <p>${pageSubheading}</p>
        </c:if>
    </div>
