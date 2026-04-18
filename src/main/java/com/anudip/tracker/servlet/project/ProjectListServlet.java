package com.anudip.tracker.servlet.project;

import com.anudip.tracker.dao.ClientDao;
import com.anudip.tracker.dao.ProjectDao;
import com.anudip.tracker.model.Project;
import com.anudip.tracker.servlet.BaseServlet;
import com.anudip.tracker.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/projects")
public class ProjectListServlet extends BaseServlet {
    private final ProjectDao projectDao = new ProjectDao();
    private final ClientDao clientDao = new ClientDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);
        request.setAttribute("projects", projectDao.findAllByUser(userId));
        request.setAttribute("clients", clientDao.findAllByUser(userId));

        int editId = RequestUtil.parseInt(request.getParameter("editId"), -1);
        if (editId > 0) {
            Project editingProject = projectDao.findByIdAndUser(editId, userId);
            request.setAttribute("editingProject", editingProject);
        }

        request.getRequestDispatcher("/WEB-INF/views/projects.jsp").forward(request, response);
    }
}
