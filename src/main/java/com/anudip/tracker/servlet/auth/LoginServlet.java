package com.anudip.tracker.servlet.auth;

import com.anudip.tracker.dao.UserDao;
import com.anudip.tracker.model.User;
import com.anudip.tracker.servlet.BaseServlet;
import com.anudip.tracker.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (getUserId(request) > 0) {
            redirect(request, response, "/dashboard");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = trim(request.getParameter("email")).toLowerCase();
        String password = trim(request.getParameter("password"));

        if (email.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Email and password are required.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }

        User user = userDao.findByEmail(email);
        if (user == null || !PasswordUtil.matches(password, user.getPasswordHash())) {
            request.setAttribute("error", "Invalid credentials.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("fullName", user.getFullName());

        redirect(request, response, "/dashboard");
    }
}
