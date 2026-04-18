<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Clients"/>
<c:set var="pageHeading" value="Client Management"/>
<c:set var="pageSubheading" value="Organize your contacts and company details in one place."/>
<jsp:include page="/WEB-INF/views/partials/app-header.jsp"/>

<div class="grid grid-2" style="margin-top: 18px;">
    <section class="card">
        <h2 class="panel-title">
            <c:choose>
                <c:when test="${not empty editingClient}">Edit Client</c:when>
                <c:otherwise>Add Client</c:otherwise>
            </c:choose>
        </h2>

        <form method="post" action="${pageContext.request.contextPath}${not empty editingClient ? '/clients/update' : '/clients/create'}">
            <c:if test="${not empty editingClient}">
                <input type="hidden" name="id" value="${editingClient.id}">
            </c:if>

            <div class="form-grid">
                <div class="field">
                    <label for="name">Name *</label>
                    <input id="name" type="text" name="name" value="${editingClient.name}" required>
                </div>
                <div class="field">
                    <label for="company">Company</label>
                    <input id="company" type="text" name="company" value="${editingClient.company}">
                </div>
                <div class="field">
                    <label for="email">Email</label>
                    <input id="email" type="email" name="email" value="${editingClient.email}">
                </div>
                <div class="field">
                    <label for="phone">Phone</label>
                    <input id="phone" type="text" name="phone" value="${editingClient.phone}">
                </div>
                <div class="field full">
                    <label for="notes">Notes</label>
                    <textarea id="notes" name="notes">${editingClient.notes}</textarea>
                </div>
            </div>

            <div class="actions" style="margin-top: 12px;">
                <button class="btn primary" type="submit">
                    <c:choose>
                        <c:when test="${not empty editingClient}">Update Client</c:when>
                        <c:otherwise>Add Client</c:otherwise>
                    </c:choose>
                </button>
                <c:if test="${not empty editingClient}">
                    <a class="btn secondary" href="${pageContext.request.contextPath}/clients">Cancel Edit</a>
                </c:if>
            </div>
        </form>
    </section>

    <section class="card">
        <h2 class="panel-title">Client Directory</h2>
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Company</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty clients}">
                        <tr>
                            <td colspan="5">No clients yet. Add your first client.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${clients}" var="client">
                            <tr>
                                <td>${client.name}</td>
                                <td>${client.email}</td>
                                <td>${client.phone}</td>
                                <td>${client.company}</td>
                                <td>
                                    <div class="actions">
                                        <a class="btn linkish" href="${pageContext.request.contextPath}/clients?editId=${client.id}">Edit</a>
                                        <form method="post" action="${pageContext.request.contextPath}/clients/delete" class="inline"
                                              onsubmit="return confirm('Delete this client? This may remove linked projects too.');">
                                            <input type="hidden" name="id" value="${client.id}">
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
</div>

<jsp:include page="/WEB-INF/views/partials/app-footer.jsp"/>
