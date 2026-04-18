<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Payments"/>
<c:set var="pageHeading" value="Payment Tracking"/>
<c:set var="pageSubheading" value="Record invoices and instantly monitor due balances."/>
<jsp:include page="/WEB-INF/views/partials/app-header.jsp"/>

<div class="grid grid-2" style="margin-top: 18px;">
    <section class="card">
        <h2 class="panel-title">Add Payment Entry</h2>

        <c:choose>
            <c:when test="${empty projects}">
                <p>No projects available. Create a project first before adding payment records.</p>
                <a class="btn primary" href="${pageContext.request.contextPath}/projects">Go to Projects</a>
            </c:when>
            <c:otherwise>
                <form method="post" action="${pageContext.request.contextPath}/payments/create">
                    <div class="form-grid">
                        <div class="field full">
                            <label for="projectId">Project *</label>
                            <select id="projectId" name="projectId" required>
                                <option value="">Select Project</option>
                                <c:forEach items="${projects}" var="project">
                                    <option value="${project.id}">${project.projectName} (${project.clientName})</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="field">
                            <label for="totalAmount">Total Amount *</label>
                            <input id="totalAmount" type="number" min="0" step="0.01" name="totalAmount" required>
                        </div>

                        <div class="field">
                            <label for="paidAmount">Paid Amount *</label>
                            <input id="paidAmount" type="number" min="0" step="0.01" name="paidAmount" required>
                        </div>

                        <div class="field">
                            <label for="paymentDate">Payment Date</label>
                            <input id="paymentDate" type="date" name="paymentDate">
                        </div>

                        <div class="field">
                            <label for="note">Note</label>
                            <input id="note" type="text" name="note">
                        </div>
                    </div>

                    <div class="actions" style="margin-top: 12px;">
                        <button class="btn primary" type="submit">Save Payment</button>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="card">
        <h2 class="panel-title">Payment Ledger</h2>
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Project</th>
                    <th>Total</th>
                    <th>Paid</th>
                    <th>Due</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty payments}">
                        <tr>
                            <td colspan="6">No payment entries yet.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${payments}" var="payment">
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
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/payments/delete" class="inline"
                                          onsubmit="return confirm('Delete this payment record?');">
                                        <input type="hidden" name="id" value="${payment.id}">
                                        <button class="btn warn" type="submit">Delete</button>
                                    </form>
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
