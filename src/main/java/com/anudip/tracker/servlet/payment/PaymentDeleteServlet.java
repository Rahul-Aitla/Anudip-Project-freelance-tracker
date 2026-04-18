package com.anudip.tracker.servlet.payment;

import com.anudip.tracker.dao.PaymentDao;
import com.anudip.tracker.servlet.BaseServlet;
import com.anudip.tracker.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/payments/delete")
public class PaymentDeleteServlet extends BaseServlet {
    private final PaymentDao paymentDao = new PaymentDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);
        int paymentId = RequestUtil.parseInt(request.getParameter("id"), -1);

        if (paymentId > 0) {
            paymentDao.delete(paymentId, userId);
        }

        int returnProjectId = RequestUtil.parseInt(request.getParameter("returnProjectId"), -1);
        if (returnProjectId > 0) {
            redirect(request, response, "/projects/details?id=" + returnProjectId);
            return;
        }

        redirect(request, response, "/payments");
    }
}
