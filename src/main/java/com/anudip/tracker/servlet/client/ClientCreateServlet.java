package com.anudip.tracker.servlet.client;

import com.anudip.tracker.dao.ClientDao;
import com.anudip.tracker.model.Client;
import com.anudip.tracker.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/clients/create")
public class ClientCreateServlet extends BaseServlet {
    private final ClientDao clientDao = new ClientDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);

        Client client = new Client();
        client.setUserId(userId);
        client.setName(trim(request.getParameter("name")));
        client.setEmail(trim(request.getParameter("email")));
        client.setPhone(trim(request.getParameter("phone")));
        client.setCompany(trim(request.getParameter("company")));
        client.setNotes(trim(request.getParameter("notes")));

        if (!client.getName().isEmpty()) {
            clientDao.create(client);
        }

        redirect(request, response, "/clients");
    }
}
