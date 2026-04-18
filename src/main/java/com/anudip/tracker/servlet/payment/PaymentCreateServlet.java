package com.anudip.tracker.servlet.payment;

import com.anudip.tracker.dao.PaymentDao;
import com.anudip.tracker.model.Payment;
import com.anudip.tracker.servlet.BaseServlet;
import com.anudip.tracker.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/payments/create")
public class PaymentCreateServlet extends BaseServlet {
    private final PaymentDao paymentDao = new PaymentDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);
        int projectId = RequestUtil.parseInt(request.getParameter("projectId"), -1);

        BigDecimal totalAmount = RequestUtil.parseBigDecimal(request.getParameter("totalAmount"));
        BigDecimal paidAmount = RequestUtil.parseBigDecimal(request.getParameter("paidAmount"));

        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            totalAmount = BigDecimal.ZERO;
        }
        if (paidAmount.compareTo(BigDecimal.ZERO) < 0) {
            paidAmount = BigDecimal.ZERO;
        }
        if (paidAmount.compareTo(totalAmount) > 0) {
            paidAmount = totalAmount;
        }

        if (projectId > 0) {
            Payment payment = new Payment();
            payment.setUserId(userId);
            payment.setProjectId(projectId);
            payment.setTotalAmount(totalAmount);
            payment.setPaidAmount(paidAmount);
            payment.setPaymentDate(RequestUtil.parseDate(request.getParameter("paymentDate")));
            payment.setNote(trim(request.getParameter("note")));
            paymentDao.create(payment);
        }

        int returnProjectId = RequestUtil.parseInt(request.getParameter("returnProjectId"), -1);
        if (returnProjectId > 0) {
            redirect(request, response, "/projects/details?id=" + returnProjectId);
            return;
        }

        redirect(request, response, "/payments");
    }
}
