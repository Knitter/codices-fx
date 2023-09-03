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

import eu.sergiolopes.codices.models.Collection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CollectionRepository implements Repository<Collection> {

    private Connection connection;
    private String tableName = "collection";

    public CollectionRepository(Connection connection) {
        this.connection = connection;
    }

    public Collection find(int id) {
        String query = "SELECT * FROM " + tableName + " WHERE id = " + id;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Collection(rs.getInt("id"), rs.getString("name"),
                        rs.getString("publishDate"), rs.getInt("publishYear"), rs.getString("description"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ObservableList<Collection> findAll() {
        String query = "SELECT * FROM " + tableName + " ORDER BY name";
        List<Collection> collections = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                collections.add(new Collection(rs.getInt("id"), rs.getString("name"),
                        rs.getString("publishDate"), rs.getInt("publishYear"),
                        rs.getString("description")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(collections);
    }

    @Override
    public List<Collection> findAllForOwner(int ownerId) {
        String query = "SELECT * FROM " + tableName + " WHERE ownedById = " + ownerId + " ORDER BY name";
        List<Collection> collections = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                collections.add(new Collection(rs.getInt("id"), rs.getString("name"),
                        rs.getString("publishDate"), rs.getInt("publishYear"),
                        rs.getString("description")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(collections);
    }

    @Override
    public ObservableList<Collection> list(int page, int size) {
        String query = "SELECT * FROM " + tableName + " ORDER BY name LIMIT " + size + " OFFSET " + page;
        List<Collection> collections = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                collections.add(new Collection(rs.getInt("id"), rs.getString("name"),
                        rs.getString("publishDate"), rs.getInt("publishYear"), rs.getString("description")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(collections);
    }

    @Override
    public List<Collection> listForOwner(int ownerId, int page, int size) {
        String query = "SELECT * FROM " + tableName + " WHERE ownedById = " + ownerId + " ORDER BY name LIMIT "
                + size + " OFFSET " + page;

        List<Collection> collections = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                collections.add(new Collection(rs.getInt("id"), rs.getString("name"),
                        rs.getString("publishDate"), rs.getInt("publishYear"),
                        rs.getString("description")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(collections);
    }

    @Override
    public boolean save(Collection obj) {
        if (obj.getId() <= 0) {
            return this.insert(obj);
        }

        return this.update(obj);
    }

    public boolean insert(Collection obj) {
        if (obj.getId() > 0) {
            return false;
        }

        String insertQry = "INSERT INTO " + tableName + "(name, ownedById, publishDate, publishYear, description) VALUES (?, 1, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(insertQry, new String[]{"id"});
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getPublishDate());
            statement.setInt(3, obj.getPublishYear());
            statement.setString(4, obj.getDescription());

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

    public boolean update(Collection obj) {
        if (obj.getId() <= 0) {
            return false;
        }

        String updateQry = "UPDATE " + tableName + " SET name = ?, publishDate = ?, publishYear = ?, description = ? WHERE id = " + obj.getId();
        try {
            PreparedStatement statement = connection.prepareStatement(updateQry);
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getPublishDate());
            statement.setInt(3, obj.getPublishYear());
            statement.setString(4, obj.getDescription());

            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Collection obj) {
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
