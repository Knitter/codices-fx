/*
 * Codices-fx, personal library manager (ebooks, audio & paper books).
 * Copyright (C) 2023  Sérgio Lopes, https:www.sergiolopes.eu, knitter.is@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.sergiolopes.codices.repositories;

import eu.sergiolopes.codices.models.Publisher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublisherRepository implements Repository<Publisher> {
    private Connection connection;
    private String tableName = "publisher";

    public PublisherRepository(Connection connection) {
        this.connection = connection;
    }

    public Publisher find(int id) {
        String query = "SELECT * FROM " + tableName + " WHERE id = " + id;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Publisher(rs.getInt("id"), rs.getString("name"),
                        rs.getString("summary"), rs.getString("website"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ObservableList<Publisher> findAll() {
        String query = "SELECT * FROM " + tableName + " ORDER BY name";
        List<Publisher> publishers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                publishers.add(new Publisher(rs.getInt("id"), rs.getString("name"),
                        rs.getString("summary"), rs.getString("website")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(publishers);
    }

    @Override
    public List<Publisher> findAllForOwner(int ownerId) {
        String query = "SELECT * FROM " + tableName + " WHERE ownedById = " + ownerId + " ORDER BY name";
        List<Publisher> publishers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                publishers.add(new Publisher(rs.getInt("id"), rs.getString("name"),
                        rs.getString("summary"), rs.getString("website")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(publishers);
    }

    @Override
    public ObservableList<Publisher> list(int page, int size) {
        String query = "SELECT * FROM " + tableName + " ORDER BY name LIMIT " + size + " OFFSET " + page;
        List<Publisher> publishers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                publishers.add(new Publisher(rs.getInt("id"), rs.getString("name"),
                        rs.getString("summary"), rs.getString("website")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(publishers);
    }

    @Override
    public List<Publisher> listForOwner(int ownerId, int page, int size) {
        String query = "SELECT * FROM " + tableName + " WHERE ownedById = " + ownerId + " ORDER BY name LIMIT "
                + size + " OFFSET " + page;

        List<Publisher> publishers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                publishers.add(new Publisher(rs.getInt("id"), rs.getString("name"),
                        rs.getString("summary"), rs.getString("website")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(publishers);
    }

    @Override
    public boolean save(Publisher obj) {
        if (obj.getId() <= 0) {
            return this.insert(obj);
        }

        return this.update(obj);
    }

    @Override
    public boolean insert(Publisher obj) {
        if (obj.getId() > 0) {
            return false;
        }

        String insertQry = "INSERT INTO " + tableName + "(name, ownedById, summary, website) VALUES (?, 1, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(insertQry, new String[]{"id"});
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getSummary());
            statement.setString(3, obj.getWebsite());

            if (statement.executeUpdate() <= 0) {
                return false;
            }

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                obj.setId(keys.getInt("id"));
            }

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Publisher obj) {
        if (obj.getId() <= 0) {
            return false;
        }

        String updateQry = "UPDATE " + tableName + " SET name = ?, summary = ?, website = ? WHERE id = " + obj.getId();
        try {
            PreparedStatement statement = connection.prepareStatement(updateQry);
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getSummary());
            statement.setString(3, obj.getWebsite());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Publisher obj) {
        if (obj.getId() <= 0) {
            return false;
        }

        String deleteQry = "DELETE FROM " + tableName + " WHERE id = " + obj.getId();
        try {
            PreparedStatement statement = connection.prepareStatement(deleteQry);
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
