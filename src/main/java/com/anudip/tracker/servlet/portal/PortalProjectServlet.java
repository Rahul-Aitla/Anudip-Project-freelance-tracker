package com.anudip.tracker.servlet.portal;

import com.anudip.tracker.dao.PaymentDao;
import com.anudip.tracker.dao.ProjectDao;
import com.anudip.tracker.dao.TaskDao;
import com.anudip.tracker.model.Project;
import com.anudip.tracker.util.PortalLinkUtil;
import com.anudip.tracker.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/portal/project")
public class PortalProjectServlet extends HttpServlet {
    private final ProjectDao projectDao = new ProjectDao();
    private final TaskDao taskDao = new TaskDao();
    private final PaymentDao paymentDao = new PaymentDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int projectId = RequestUtil.parseInt(request.getParameter("id"), -1);
        String token = request.getParameter("token");

        if (projectId <= 0 || token == null || token.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Project project = projectDao.findById(projectId);
        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (!PortalLinkUtil.isTokenValid(token, projectId, project.getUserId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid client portal link.");
            return;
        }

        request.setAttribute("project", project);
        request.setAttribute("tasks", taskDao.findByProject(project.getUserId(), projectId));
        request.setAttribute("payments", paymentDao.findByProject(project.getUserId(), projectId));

        request.getRequestDispatcher("/WEB-INF/views/portal/project.jsp").forward(request, response);
    }
}
