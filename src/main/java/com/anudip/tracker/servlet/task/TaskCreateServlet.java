package com.anudip.tracker.servlet.task;

import com.anudip.tracker.dao.TaskDao;
import com.anudip.tracker.model.Task;
import com.anudip.tracker.servlet.BaseServlet;
import com.anudip.tracker.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/tasks/create")
public class TaskCreateServlet extends BaseServlet {
    private final TaskDao taskDao = new TaskDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);
        int projectId = RequestUtil.parseInt(request.getParameter("projectId"), -1);
        String title = trim(request.getParameter("title"));

        if (projectId > 0 && !title.isEmpty()) {
            Task task = new Task();
            task.setUserId(userId);
            task.setProjectId(projectId);
            task.setTitle(title);
            task.setCompleted(false);
            task.setPriority(resolvePriority(trim(request.getParameter("priority"))));
            task.setDueDate(RequestUtil.parseDate(request.getParameter("dueDate")));
            taskDao.create(task);
        }

        redirect(request, response, "/projects/details?id=" + projectId);
    }

    private String resolvePriority(String priority) {
        if ("Low".equals(priority) || "High".equals(priority)) {
            return priority;
        }
        return "Medium";
    }
}
