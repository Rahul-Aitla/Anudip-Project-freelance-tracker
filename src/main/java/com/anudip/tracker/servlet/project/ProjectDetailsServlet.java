package com.anudip.tracker.servlet.project;

import com.anudip.tracker.dao.PaymentDao;
import com.anudip.tracker.dao.ProjectDao;
import com.anudip.tracker.dao.TaskDao;
import com.anudip.tracker.model.Project;
import com.anudip.tracker.servlet.BaseServlet;
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

        request.getRequestDispatcher("/WEB-INF/views/project-details.jsp").forward(request, response);
    }
}
