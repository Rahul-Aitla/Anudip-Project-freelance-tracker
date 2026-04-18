<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Project Details"/>
<c:set var="pageHeading" value="${project.projectName}"/>
<c:set var="pageSubheading" value="Client: ${project.clientName} | Deadline: ${project.deadline}"/>
<jsp:include page="/WEB-INF/views/partials/app-header.jsp"/>

<section class="card" style="margin-top: 18px;">
    <h2 class="panel-title">Project Snapshot</h2>
    <div class="grid grid-2">
        <div>
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

<section class="card" style="margin-top: 16px;">
    <h2 class="panel-title">Client Portal Link</h2>
    <p>Share this read-only link with your client.</p>

    <div class="form-grid">
        <div class="field full">
            <label for="portalLink">Share Link</label>
            <input id="portalLink" type="text" value="${portalLink}" readonly>
        </div>
    </div>

    <div class="actions" style="margin-top: 10px;">
        <button class="btn secondary" type="button" onclick="copyPortalLink()">Copy Link</button>
        <a class="btn linkish" target="_blank" rel="noopener"
           href="${pageContext.request.contextPath}/portal/project?id=${project.id}&token=${portalToken}">Preview Portal</a>
    </div>
</section>

<div class="grid grid-2" style="margin-top: 16px;">
    <section class="card">
        <h2 class="panel-title">Task Tracking</h2>

        <form method="post" action="${pageContext.request.contextPath}/tasks/create">
            <input type="hidden" name="projectId" value="${project.id}">
            <div class="form-grid">
                <div class="field full">
                    <label for="taskTitle">Task Title *</label>
                    <input id="taskTitle" type="text" name="title" required>
                </div>
                <div class="field">
                    <label for="taskPriority">Priority</label>
                    <select id="taskPriority" name="priority">
                        <option value="Low">Low</option>
                        <option value="Medium" selected>Medium</option>
                        <option value="High">High</option>
                    </select>
                </div>
                <div class="field">
                    <label for="taskDueDate">Due Date</label>
                    <input id="taskDueDate" type="date" name="dueDate">
                </div>
            </div>
            <div class="actions" style="margin-top: 12px;">
                <button class="btn primary" type="submit">Add Task</button>
            </div>
        </form>

        <div class="table-wrap" style="margin-top: 12px;">
            <table>
                <thead>
                <tr>
                    <th>Task</th>
                    <th>Priority</th>
                    <th>Due</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty tasks}">
                        <tr>
                            <td colspan="5">No tasks yet for this project.</td>
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
                                <td>
                                    <div class="actions">
                                        <form method="post" action="${pageContext.request.contextPath}/tasks/toggle" class="inline">
                                            <input type="hidden" name="id" value="${task.id}">
                                            <input type="hidden" name="projectId" value="${project.id}">
                                            <button class="btn secondary" type="submit">Toggle</button>
                                        </form>
                                        <form method="post" action="${pageContext.request.contextPath}/tasks/delete" class="inline"
                                              onsubmit="return confirm('Delete this task?');">
                                            <input type="hidden" name="id" value="${task.id}">
                                            <input type="hidden" name="projectId" value="${project.id}">
                                            <button class="btn warn" type="submit">Delete</button>
                                        </form>
                                    </div>
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
        <h2 class="panel-title">Payment Tracking</h2>

        <form method="post" action="${pageContext.request.contextPath}/payments/create">
            <input type="hidden" name="projectId" value="${project.id}">
            <input type="hidden" name="returnProjectId" value="${project.id}">
            <div class="form-grid">
                <div class="field">
                    <label for="totalAmount">Total Amount *</label>
                    <input id="totalAmount" type="number" step="0.01" min="0" name="totalAmount" required>
                </div>
                <div class="field">
                    <label for="paidAmount">Paid Amount *</label>
                    <input id="paidAmount" type="number" step="0.01" min="0" name="paidAmount" required>
                </div>
                <div class="field">
                    <label for="paymentDate">Payment Date</label>
                    <input id="paymentDate" type="date" name="paymentDate">
                </div>
                <div class="field">
                    <label for="paymentNote">Note</label>
                    <input id="paymentNote" type="text" name="note">
                </div>
            </div>
            <div class="actions" style="margin-top: 12px;">
                <button class="btn primary" type="submit">Add Payment Entry</button>
            </div>
        </form>

        <div class="table-wrap" style="margin-top: 12px;">
            <table>
                <thead>
                <tr>
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
                            <td colspan="5">No payment entries yet.</td>
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
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/payments/delete" class="inline"
                                          onsubmit="return confirm('Delete this payment entry?');">
                                        <input type="hidden" name="id" value="${payment.id}">
                                        <input type="hidden" name="returnProjectId" value="${project.id}">
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

<script>
    function copyPortalLink() {
        const input = document.getElementById('portalLink');
        if (!input) {
            return;
        }
        input.select();
        input.setSelectionRange(0, 99999);
        document.execCommand('copy');
    }
</script>

<jsp:include page="/WEB-INF/views/partials/app-footer.jsp"/>
