package com.anudip.tracker.servlet.dashboard;

import com.anudip.tracker.dao.DashboardDao;
import com.anudip.tracker.dao.PaymentDao;
import com.anudip.tracker.dao.ProjectDao;
import com.anudip.tracker.model.DashboardStats;
import com.anudip.tracker.model.Project;
import com.anudip.tracker.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends BaseServlet {
    private final DashboardDao dashboardDao = new DashboardDao();
    private final ProjectDao projectDao = new ProjectDao();
    private final PaymentDao paymentDao = new PaymentDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);

        DashboardStats stats = dashboardDao.loadStats(userId);
        List<Project> recentProjects = projectDao.findAllByUser(userId);

        int overdueAlerts = 0;
        int dueSoonAlerts = 0;
        for (Project project : recentProjects) {
            if ("overdue".equals(project.getDeadlineAlert())) {
                overdueAlerts++;
            } else if ("soon".equals(project.getDeadlineAlert())) {
                dueSoonAlerts++;
            }
        }

        request.setAttribute("stats", stats);
        request.setAttribute("recentProjects", recentProjects);
        request.setAttribute("recentPayments", paymentDao.findAllByUser(userId));
        request.setAttribute("overdueAlerts", overdueAlerts);
        request.setAttribute("dueSoonAlerts", dueSoonAlerts);
        request.setAttribute("deadlineAlerts", overdueAlerts + dueSoonAlerts);

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
