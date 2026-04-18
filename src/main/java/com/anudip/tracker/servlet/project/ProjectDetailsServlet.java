package com.anudip.tracker.servlet.project;

import com.anudip.tracker.dao.PaymentDao;
import com.anudip.tracker.dao.ProjectDao;
import com.anudip.tracker.dao.TaskDao;
import com.anudip.tracker.model.Project;
import com.anudip.tracker.servlet.BaseServlet;
import com.anudip.tracker.util.PortalLinkUtil;
import com.anudip.tracker.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/projects/details")
public class ProjectDetailsServlet extends BaseServlet {
    private final ProjectDao projectDao = new ProjectDao();
    private final TaskDao taskDao = new TaskDao();
    private final PaymentDao paymentDao = new PaymentDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);
        int projectId = RequestUtil.parseInt(request.getParameter("id"), -1);

        if (projectId <= 0) {
            redirect(request, response, "/projects");
            return;
        }

        Project project = projectDao.findByIdAndUser(projectId, userId);
        if (project == null) {
            redirect(request, response, "/projects");
            return;
        }

        request.setAttribute("project", project);
        request.setAttribute("tasks", taskDao.findByProject(userId, projectId));
        request.setAttribute("payments", paymentDao.findByProject(userId, projectId));

        String portalToken = PortalLinkUtil.generateToken(project.getId(), project.getUserId());
        request.setAttribute("portalToken", portalToken);
        request.setAttribute("portalLink", buildPortalLink(request, project.getId(), portalToken));

        request.getRequestDispatcher("/WEB-INF/views/project-details.jsp").forward(request, response);
    }

    private String buildPortalLink(HttpServletRequest request, int projectId, String token) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        boolean defaultPort = ("http".equalsIgnoreCase(scheme) && serverPort == 80)
                || ("https".equalsIgnoreCase(scheme) && serverPort == 443);
        String host = defaultPort ? serverName : serverName + ":" + serverPort;
        return scheme + "://" + host + request.getContextPath() + "/portal/project?id=" + projectId + "&token=" + token;
    }
}
