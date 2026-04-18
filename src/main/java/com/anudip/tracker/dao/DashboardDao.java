package com.anudip.tracker.dao;

import com.anudip.tracker.model.DashboardStats;
import com.anudip.tracker.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDao {
    public DashboardStats loadStats(int userId) {
        DashboardStats stats = new DashboardStats();
        stats.setTotalClients(fetchCount("SELECT COUNT(*) AS count FROM clients WHERE user_id = ?", userId));
        stats.setActiveProjects(fetchCount("SELECT COUNT(*) AS count FROM projects WHERE user_id = ? AND status IN ('Pending', 'In Progress')", userId));
        stats.setCompletedProjects(fetchCount("SELECT COUNT(*) AS count FROM projects WHERE user_id = ? AND status = 'Completed'", userId));
        stats.setPendingPayments(fetchPendingAmount(userId));
        return stats;
    }

    private int fetchCount(String sql, int userId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (SQLException ex) {
            return 0;
        }
        return 0;
    }

    private BigDecimal fetchPendingAmount(int userId) {
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
}
