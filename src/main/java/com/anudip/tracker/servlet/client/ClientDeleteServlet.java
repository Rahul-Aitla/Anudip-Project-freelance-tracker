package com.anudip.tracker.servlet.client;

import com.anudip.tracker.dao.ClientDao;
import com.anudip.tracker.servlet.BaseServlet;
import com.anudip.tracker.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/clients/delete")
public class ClientDeleteServlet extends BaseServlet {
    private final ClientDao clientDao = new ClientDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);
        int clientId = RequestUtil.parseInt(request.getParameter("id"), -1);

        if (clientId > 0) {
            clientDao.delete(clientId, userId);
        }

        redirect(request, response, "/clients");
    }
}
