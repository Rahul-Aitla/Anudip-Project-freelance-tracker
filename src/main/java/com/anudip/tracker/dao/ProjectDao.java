package com.anudip.tracker.dao;

import com.anudip.tracker.model.Project;
import com.anudip.tracker.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {
    public List<Project> findAllByUser(int userId) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT p.id, p.user_id, p.client_id, c.name AS client_name, p.project_name, p.description, p.start_date, p.deadline, p.status, p.progress_percent " +
                "FROM projects p JOIN clients c ON p.client_id = c.id WHERE p.user_id = ? ORDER BY p.deadline IS NULL, p.deadline ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    projects.add(mapProject(rs));
                }
            }
        } catch (SQLException ex) {
            return projects;
        }
        return projects;
    }

    public Project findByIdAndUser(int projectId, int userId) {
        String sql = "SELECT p.id, p.user_id, p.client_id, c.name AS client_name, p.project_name, p.description, p.start_date, p.deadline, p.status, p.progress_percent " +
                "FROM projects p JOIN clients c ON p.client_id = c.id WHERE p.id = ? AND p.user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, projectId);
            statement.setInt(2, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapProject(rs);
                }
            }
        } catch (SQLException ex) {
            return null;
        }
        return null;
    }

    public boolean create(Project project) {
        String sql = "INSERT INTO projects(user_id, client_id, project_name, description, start_date, deadline, status, progress_percent) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, project.getUserId());
            statement.setInt(2, project.getClientId());
            statement.setString(3, project.getProjectName());
            statement.setString(4, project.getDescription());
            statement.setDate(5, project.getStartDate());
            statement.setDate(6, project.getDeadline());
            statement.setString(7, project.getStatus());
            statement.setInt(8, project.getProgressPercent());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean update(Project project) {
        String sql = "UPDATE projects SET client_id = ?, project_name = ?, description = ?, start_date = ?, deadline = ?, status = ?, progress_percent = ? " +
                "WHERE id = ? AND user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, project.getClientId());
            statement.setString(2, project.getProjectName());
            statement.setString(3, project.getDescription());
            statement.setDate(4, project.getStartDate());
            statement.setDate(5, project.getDeadline());
            statement.setString(6, project.getStatus());
            statement.setInt(7, project.getProgressPercent());
            statement.setInt(8, project.getId());
            statement.setInt(9, project.getUserId());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean delete(int projectId, int userId) {
        String sql = "DELETE FROM projects WHERE id = ? AND user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, projectId);
            statement.setInt(2, userId);
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            return false;
        }
    }

    private Project mapProject(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setId(rs.getInt("id"));
        project.setUserId(rs.getInt("user_id"));
        project.setClientId(rs.getInt("client_id"));
        project.setClientName(rs.getString("client_name"));
        project.setProjectName(rs.getString("project_name"));
        project.setDescription(rs.getString("description"));
        project.setStartDate(rs.getDate("start_date"));
        project.setDeadline(rs.getDate("deadline"));
        project.setStatus(rs.getString("status"));
        project.setProgressPercent(rs.getInt("progress_percent"));
        return project;
    }
}
