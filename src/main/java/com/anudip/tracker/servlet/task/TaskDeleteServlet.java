package com.anudip.tracker.servlet.task;

import com.anudip.tracker.dao.TaskDao;
import com.anudip.tracker.servlet.BaseServlet;
import com.anudip.tracker.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/tasks/delete")
public class TaskDeleteServlet extends BaseServlet {
    private final TaskDao taskDao = new TaskDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);
        int taskId = RequestUtil.parseInt(request.getParameter("id"), -1);
        int projectId = RequestUtil.parseInt(request.getParameter("projectId"), -1);

        if (taskId > 0) {
            taskDao.delete(taskId, userId);
        }

        redirect(request, response, "/projects/details?id=" + projectId);
    }
}
