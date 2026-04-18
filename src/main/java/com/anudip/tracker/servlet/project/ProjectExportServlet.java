package com.anudip.tracker.servlet.project;

import com.anudip.tracker.dao.ProjectDao;
import com.anudip.tracker.model.Project;
import com.anudip.tracker.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/projects/export")
public class ProjectExportServlet extends BaseServlet {
    private final ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);
        String searchQuery = trim(request.getParameter("q"));
        String statusFilter = trim(request.getParameter("statusFilter"));

        List<Project> projects = projectDao.findByUserWithFilters(userId, searchQuery, statusFilter);

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=project-report.csv");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Project Name,Client,Status,Progress %,Start Date,Deadline,Deadline Alert");
            for (Project project : projects) {
                writer.println(
                        csv(project.getProjectName()) + "," +
                        csv(project.getClientName()) + "," +
                        csv(project.getStatus()) + "," +
                        project.getProgressPercent() + "," +
                        csv(project.getStartDate() == null ? "" : project.getStartDate().toString()) + "," +
                        csv(project.getDeadline() == null ? "" : project.getDeadline().toString()) + "," +
                        csv(project.getDeadlineAlertLabel())
                );
            }
        }
    }

    private String csv(String value) {
        if (value == null) {
            return "\"\"";
        }
        String escaped = value.replace("\r", " ")
                .replace("\n", " ")
                .replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
