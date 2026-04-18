package com.anudip.tracker.servlet.client;

import com.anudip.tracker.dao.ClientDao;
import com.anudip.tracker.model.Client;
import com.anudip.tracker.servlet.BaseServlet;
import com.anudip.tracker.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/clients")
public class ClientListServlet extends BaseServlet {
    private final ClientDao clientDao = new ClientDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);
        List<Client> clients = clientDao.findAllByUser(userId);

        int editId = RequestUtil.parseInt(request.getParameter("editId"), -1);
        if (editId > 0) {
            Client editingClient = clientDao.findByIdAndUser(editId, userId);
            request.setAttribute("editingClient", editingClient);
        }

        request.setAttribute("clients", clients);
        request.getRequestDispatcher("/WEB-INF/views/clients.jsp").forward(request, response);
    }
}
