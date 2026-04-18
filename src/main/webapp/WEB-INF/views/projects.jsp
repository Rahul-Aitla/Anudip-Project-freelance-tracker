<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Projects"/>
<c:set var="pageHeading" value="Project Management"/>
<c:set var="pageSubheading" value="Assign projects to clients, track status, and monitor deadlines."/>
<jsp:include page="/WEB-INF/views/partials/app-header.jsp"/>

<div class="grid grid-2" style="margin-top: 18px;">
    <section class="card">
        <h2 class="panel-title">
            <c:choose>
                <c:when test="${not empty editingProject}">Edit Project</c:when>
                <c:otherwise>Create Project</c:otherwise>
            </c:choose>
        </h2>

        <c:choose>
            <c:when test="${empty clients}">
                <p>Add at least one client before creating projects.</p>
                <a class="btn primary" href="${pageContext.request.contextPath}/clients">Go to Clients</a>
            </c:when>
            <c:otherwise>
                <form method="post" action="${pageContext.request.contextPath}${not empty editingProject ? '/projects/update' : '/projects/create'}">
                    <c:if test="${not empty editingProject}">
                        <input type="hidden" name="id" value="${editingProject.id}">
                    </c:if>

                    <div class="form-grid">
                        <div class="field full">
                            <label for="projectName">Project Name *</label>
                            <input id="projectName" type="text" name="projectName" value="${editingProject.projectName}" required>
                        </div>

                        <div class="field">
                            <label for="clientId">Client *</label>
                            <select id="clientId" name="clientId" required>
                                <option value="">Select Client</option>
                                <c:forEach items="${clients}" var="client">
                                    <option value="${client.id}" ${editingProject.clientId == client.id ? 'selected' : ''}>${client.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="field">
                            <label for="status">Status</label>
                            <select id="status" name="status">
                                <option value="Pending" ${editingProject.status == 'Pending' ? 'selected' : ''}>Pending</option>
                                <option value="In Progress" ${editingProject.status == 'In Progress' ? 'selected' : ''}>In Progress</option>
                                <option value="Completed" ${editingProject.status == 'Completed' ? 'selected' : ''}>Completed</option>
                            </select>
                        </div>

                        <div class="field">
                            <label for="startDate">Start Date</label>
                            <input id="startDate" type="date" name="startDate" value="${editingProject.startDate}">
                        </div>

                        <div class="field">
                            <label for="deadline">Deadline</label>
                            <input id="deadline" type="date" name="deadline" value="${editingProject.deadline}">
                        </div>

                        <div class="field full">
                            <label for="description">Description</label>
                            <textarea id="description" name="description">${editingProject.description}</textarea>
                        </div>

                        <div class="field full">
                            <label for="progressPercent">Progress (%)</label>
                            <input id="progressPercent" type="number" min="0" max="100" name="progressPercent"
                                   value="${empty editingProject ? 0 : editingProject.progressPercent}">
                        </div>
                    </div>

                    <div class="actions" style="margin-top: 12px;">
                        <button class="btn primary" type="submit">
                            <c:choose>
                                <c:when test="${not empty editingProject}">Update Project</c:when>
                                <c:otherwise>Create Project</c:otherwise>
                            </c:choose>
                        </button>
                        <c:if test="${not empty editingProject}">
                            <a class="btn secondary" href="${pageContext.request.contextPath}/projects">Cancel Edit</a>
                        </c:if>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="card">
        <h2 class="panel-title">Project Pipeline</h2>
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Client</th>
                    <th>Status</th>
                    <th>Progress</th>
                    <th>Deadline</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty projects}">
                        <tr>
                            <td colspan="6">No projects created yet.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${projects}" var="project">
                            <tr>
                                <td>${project.projectName}</td>
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
                                <td>${project.progressPercent}%</td>
                                <td>${project.deadline}</td>
                                <td>
                                    <div class="actions">
                                        <a class="btn linkish" href="${pageContext.request.contextPath}/projects/details?id=${project.id}">Details</a>
                                        <a class="btn linkish" href="${pageContext.request.contextPath}/projects?editId=${project.id}">Edit</a>
                                        <form method="post" action="${pageContext.request.contextPath}/projects/delete" class="inline"
                                              onsubmit="return confirm('Delete this project and linked records?');">
                                            <input type="hidden" name="id" value="${project.id}">
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
