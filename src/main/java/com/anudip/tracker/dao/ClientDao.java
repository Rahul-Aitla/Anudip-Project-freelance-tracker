package com.anudip.tracker.dao;

import com.anudip.tracker.model.Client;
import com.anudip.tracker.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDao {
    public List<Client> findAllByUser(int userId) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT id, user_id, name, email, phone, company, notes FROM clients WHERE user_id = ? ORDER BY id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    clients.add(mapClient(rs));
                }
            }
        } catch (SQLException ex) {
            return clients;
        }
        return clients;
    }

    public Client findByIdAndUser(int clientId, int userId) {
        String sql = "SELECT id, user_id, name, email, phone, company, notes FROM clients WHERE id = ? AND user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            statement.setInt(2, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapClient(rs);
                }
            }
        } catch (SQLException ex) {
            return null;
        }
        return null;
    }

    public boolean create(Client client) {
        String sql = "INSERT INTO clients(user_id, name, email, phone, company, notes) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, client.getUserId());
            statement.setString(2, client.getName());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getPhone());
            statement.setString(5, client.getCompany());
            statement.setString(6, client.getNotes());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean update(Client client) {
        String sql = "UPDATE clients SET name = ?, email = ?, phone = ?, company = ?, notes = ? WHERE id = ? AND user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getName());
            statement.setString(2, client.getEmail());
            statement.setString(3, client.getPhone());
            statement.setString(4, client.getCompany());
            statement.setString(5, client.getNotes());
            statement.setInt(6, client.getId());
            statement.setInt(7, client.getUserId());
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean delete(int clientId, int userId) {
        String sql = "DELETE FROM clients WHERE id = ? AND user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            statement.setInt(2, userId);
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            return false;
        }
    }

    private Client mapClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setUserId(rs.getInt("user_id"));
        client.setName(rs.getString("name"));
        client.setEmail(rs.getString("email"));
        client.setPhone(rs.getString("phone"));
        client.setCompany(rs.getString("company"));
        client.setNotes(rs.getString("notes"));
        return client;
    }
}
