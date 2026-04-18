<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Client Portal - ${project.projectName}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<main class="page-wrap">
    <div class="page-heading">
        <h1>${project.projectName}</h1>
        <p>Client portal (read-only) for ${project.clientName}</p>
    </div>

    <section class="card" style="margin-top: 18px;">
        <h2 class="panel-title">Project Summary</h2>
        <div class="grid grid-2">
            <div>
                <p><strong>Client:</strong> ${project.clientName}</p>
                <p><strong>Status:</strong>
                    <c:choose>
                        <c:when test="${project.status == 'Completed'}">
                            <span class="badge complete">Completed</span>
                        </c:when>
                        <c:when test="${project.status == 'In Progress'}">
                            <span class="badge progress">In Progress</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge pending">Pending</span>
                        </c:otherwise>
                    </c:choose>
                </p>
                <p><strong>Start Date:</strong> ${project.startDate}</p>
                <p><strong>Deadline:</strong> ${project.deadline}</p>
            </div>
            <div>
                <p><strong>Description:</strong></p>
                <p>${project.description}</p>
                <p><strong>Progress:</strong> ${project.progressPercent}%</p>
                <div class="progress-wrap">
                    <div class="progress-bar" style="width: ${project.progressPercent}%;"></div>
                </div>
            </div>
        </div>
    </section>

    <div class="grid grid-2" style="margin-top: 16px;">
        <section class="card">
            <h2 class="panel-title">Tasks (Read-only)</h2>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Task</th>
                        <th>Priority</th>
                        <th>Due Date</th>
                        <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty tasks}">
                            <tr>
                                <td colspan="4">No tasks yet.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${tasks}" var="task">
                                <tr>
                                    <td>${task.title}</td>
                                    <td>${task.priority}</td>
                                    <td>${task.dueDate}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${task.completed}">
                                                <span class="badge complete">Complete</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge pending">Pending</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </section>

        <section class="card">
            <h2 class="panel-title">Payments (Read-only)</h2>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Total</th>
                        <th>Paid</th>
                        <th>Due</th>
                        <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty payments}">
                            <tr>
                                <td colspan="4">No payment records yet.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${payments}" var="payment">
                                <tr>
                                    <td>$<fmt:formatNumber value="${payment.totalAmount}" minFractionDigits="2"/></td>
                                    <td>$<fmt:formatNumber value="${payment.paidAmount}" minFractionDigits="2"/></td>
                                    <td>$<fmt:formatNumber value="${payment.dueAmount}" minFractionDigits="2"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${payment.paymentStatus == 'Paid'}">
                                                <span class="badge paid">Paid</span>
                                            </c:when>
                                            <c:when test="${payment.paymentStatus == 'Partial'}">
                                                <span class="badge partial">Partial</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge pending">Pending</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </section>
    </div>

    <p class="footer-note">This is a read-only view generated by Freelancer Tracker.</p>
</main>
</body>
</html>
