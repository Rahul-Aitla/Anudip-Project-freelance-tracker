package com.anudip.tracker.dao;

import com.anudip.tracker.model.Task;
import com.anudip.tracker.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    public List<Task> findByProject(int userId, int projectId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, user_id, project_id, title, is_completed, priority, due_date FROM tasks WHERE user_id = ? AND project_id = ? ORDER BY is_completed ASC, id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, projectId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapTask(rs));
                }
            }
        } catch (SQLException ex) {
            return tasks;
        }
        return tasks;
    }

    public boolean create(Task task) {
        String sql = "INSERT INTO tasks(user_id, project_id, title, is_completed, priority, due_date) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, task.getUserId());
            statement.setInt(2, task.getProjectId());
            statement.setString(3, task.getTitle());
            statement.setBoolean(4, task.isCompleted());
            statement.setString(5, task.getPriority());
            statement.setDate(6, task.getDueDate());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean toggle(int taskId, int userId) {
        String sql = "UPDATE tasks SET is_completed = NOT is_completed WHERE id = ? AND user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, taskId);
            statement.setInt(2, userId);
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean delete(int taskId, int userId) {
        String sql = "DELETE FROM tasks WHERE id = ? AND user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, taskId);
            statement.setInt(2, userId);
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            return false;
        }
    }

    private Task mapTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setUserId(rs.getInt("user_id"));
        task.setProjectId(rs.getInt("project_id"));
        task.setTitle(rs.getString("title"));
        task.setCompleted(rs.getBoolean("is_completed"));
        task.setPriority(rs.getString("priority"));
        task.setDueDate(rs.getDate("due_date"));
        return task;
    }
}
