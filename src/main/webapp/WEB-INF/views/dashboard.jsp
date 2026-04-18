<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Dashboard"/>
<c:set var="pageHeading" value="Freelancer Command Center"/>
<c:set var="pageSubheading" value="A live view of your clients, projects, and cashflow."/>
<jsp:include page="/WEB-INF/views/partials/app-header.jsp"/>

<div class="grid grid-4" style="margin-top: 18px;">
    <div class="card stat-card">
        <span class="label">Total Clients</span>
        <p class="value">${stats.totalClients}</p>
    </div>
    <div class="card stat-card">
        <span class="label">Active Projects</span>
        <p class="value">${stats.activeProjects}</p>
    </div>
    <div class="card stat-card">
        <span class="label">Completed Projects</span>
        <p class="value">${stats.completedProjects}</p>
    </div>
    <div class="card stat-card">
        <span class="label">Pending Payments</span>
        <p class="value">$<fmt:formatNumber value="${stats.pendingPayments}" type="number" minFractionDigits="2"/></p>
    </div>
</div>

<div class="grid grid-2" style="margin-top: 16px;">
    <section class="card">
        <h2 class="panel-title">Upcoming & Active Projects</h2>
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Project</th>
                    <th>Client</th>
                    <th>Status</th>
                    <th>Deadline</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty recentProjects}">
                        <tr>
                            <td colspan="4">No projects yet. Start by adding your first project.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${recentProjects}" var="project" begin="0" end="5">
                            <tr>
                                <td>
                                    <a href="${pageContext.request.contextPath}/projects/details?id=${project.id}">${project.projectName}</a>
                                </td>
                                <td>${project.clientName}</td>
                                <td>
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
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty project.deadline}">-</c:when>
                                        <c:otherwise>${project.deadline}</c:otherwise>
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
        <h2 class="panel-title">Latest Payment Entries</h2>
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Project</th>
                    <th>Total</th>
                    <th>Paid</th>
                    <th>Due</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty recentPayments}">
                        <tr>
                            <td colspan="5">No payment entries yet.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${recentPayments}" var="payment" begin="0" end="5">
                            <tr>
                                <td>${payment.projectName}</td>
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

<jsp:include page="/WEB-INF/views/partials/app-footer.jsp"/>
