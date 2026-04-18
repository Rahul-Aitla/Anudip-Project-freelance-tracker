package com.anudip.tracker.servlet.payment;

import com.anudip.tracker.dao.PaymentDao;
import com.anudip.tracker.dao.ProjectDao;
import com.anudip.tracker.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/payments")
public class PaymentListServlet extends BaseServlet {
    private final PaymentDao paymentDao = new PaymentDao();
    private final ProjectDao projectDao = new ProjectDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);

        request.setAttribute("payments", paymentDao.findAllByUser(userId));
        request.setAttribute("projects", projectDao.findAllByUser(userId));

        request.getRequestDispatcher("/WEB-INF/views/payments.jsp").forward(request, response);
    }
}
