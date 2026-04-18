package com.anudip.tracker.dao;

import com.anudip.tracker.model.Payment;
import com.anudip.tracker.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDao {
    public List<Payment> findAllByUser(int userId) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT py.id, py.user_id, py.project_id, p.project_name, py.total_amount, py.paid_amount, py.due_amount, py.payment_date, py.note " +
                "FROM payments py JOIN projects p ON py.project_id = p.id WHERE py.user_id = ? ORDER BY py.id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapPayment(rs));
                }
            }
        } catch (SQLException ex) {
            return payments;
        }
        return payments;
    }

    public List<Payment> findByProject(int userId, int projectId) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT py.id, py.user_id, py.project_id, p.project_name, py.total_amount, py.paid_amount, py.due_amount, py.payment_date, py.note " +
                "FROM payments py JOIN projects p ON py.project_id = p.id WHERE py.user_id = ? AND py.project_id = ? ORDER BY py.id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, projectId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapPayment(rs));
                }
            }
        } catch (SQLException ex) {
            return payments;
        }
        return payments;
    }

    public boolean create(Payment payment) {
        String sql = "INSERT INTO payments(user_id, project_id, total_amount, paid_amount, payment_date, note) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, payment.getUserId());
            statement.setInt(2, payment.getProjectId());
            statement.setBigDecimal(3, payment.getTotalAmount());
            statement.setBigDecimal(4, payment.getPaidAmount());
            statement.setDate(5, payment.getPaymentDate());
            statement.setString(6, payment.getNote());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean delete(int paymentId, int userId) {
        String sql = "DELETE FROM payments WHERE id = ? AND user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, paymentId);
            statement.setInt(2, userId);
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            return false;
        }
    }

    public BigDecimal sumPendingDueAmountByUser(int userId) {
        String sql = "SELECT COALESCE(SUM(due_amount), 0) AS pending_amount FROM payments WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("pending_amount");
                }
            }
        } catch (SQLException ex) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    private Payment mapPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setUserId(rs.getInt("user_id"));
        payment.setProjectId(rs.getInt("project_id"));
        payment.setProjectName(rs.getString("project_name"));
        payment.setTotalAmount(rs.getBigDecimal("total_amount"));
        payment.setPaidAmount(rs.getBigDecimal("paid_amount"));
        payment.setDueAmount(rs.getBigDecimal("due_amount"));
        payment.setPaymentDate(rs.getDate("payment_date"));
        payment.setNote(rs.getString("note"));
        payment.setPaymentStatus(resolveStatus(payment.getTotalAmount(), payment.getPaidAmount(), payment.getDueAmount()));
        return payment;
    }

    private String resolveStatus(BigDecimal total, BigDecimal paid, BigDecimal due) {
        if (paid.compareTo(BigDecimal.ZERO) <= 0) {
            return "Pending";
        }
        if (due.compareTo(BigDecimal.ZERO) <= 0 || paid.compareTo(total) >= 0) {
            return "Paid";
        }
        return "Partial";
    }
}
