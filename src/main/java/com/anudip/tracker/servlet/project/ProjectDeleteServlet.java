package com.anudip.tracker.servlet.project;

import com.anudip.tracker.dao.ProjectDao;
import com.anudip.tracker.servlet.BaseServlet;
import com.anudip.tracker.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/projects/delete")
public class ProjectDeleteServlet extends BaseServlet {
    private final ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);
        int projectId = RequestUtil.parseInt(request.getParameter("id"), -1);

        if (projectId > 0) {
            projectDao.delete(projectId, userId);
        }

        redirect(request, response, "/projects");
    }
}
