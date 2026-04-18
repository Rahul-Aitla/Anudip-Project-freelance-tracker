package com.anudip.tracker.servlet.dashboard;

import com.anudip.tracker.dao.DashboardDao;
import com.anudip.tracker.dao.PaymentDao;
import com.anudip.tracker.dao.ProjectDao;
import com.anudip.tracker.model.DashboardStats;
import com.anudip.tracker.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends BaseServlet {
    private final DashboardDao dashboardDao = new DashboardDao();
    private final ProjectDao projectDao = new ProjectDao();
    private final PaymentDao paymentDao = new PaymentDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);

        DashboardStats stats = dashboardDao.loadStats(userId);

        request.setAttribute("stats", stats);
        request.setAttribute("recentProjects", projectDao.findAllByUser(userId));
        request.setAttribute("recentPayments", paymentDao.findAllByUser(userId));

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
