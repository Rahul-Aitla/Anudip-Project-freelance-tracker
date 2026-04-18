package com.anudip.tracker.servlet.project;

import com.anudip.tracker.dao.ProjectDao;
import com.anudip.tracker.model.Project;
import com.anudip.tracker.servlet.BaseServlet;
import com.anudip.tracker.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/projects/create")
public class ProjectCreateServlet extends BaseServlet {
    private final ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);

        Project project = new Project();
        project.setUserId(userId);
        project.setClientId(RequestUtil.parseInt(request.getParameter("clientId"), -1));
        project.setProjectName(trim(request.getParameter("projectName")));
        project.setDescription(trim(request.getParameter("description")));
        project.setStartDate(RequestUtil.parseDate(request.getParameter("startDate")));
        project.setDeadline(RequestUtil.parseDate(request.getParameter("deadline")));
        project.setStatus(resolveStatus(trim(request.getParameter("status"))));
        project.setProgressPercent(clampProgress(RequestUtil.parseInt(request.getParameter("progressPercent"), 0)));

        if (project.getClientId() > 0 && !project.getProjectName().isEmpty()) {
            projectDao.create(project);
        }

        redirect(request, response, "/projects");
    }

    private String resolveStatus(String status) {
        if ("In Progress".equals(status) || "Completed".equals(status)) {
            return status;
        }
        return "Pending";
    }

    private int clampProgress(int value) {
        if (value < 0) {
            return 0;
        }
        return Math.min(value, 100);
    }
}
