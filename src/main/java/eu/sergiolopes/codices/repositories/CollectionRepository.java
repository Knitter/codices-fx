/**
 * MIT License
 * <p>
 * Copyright (c) 2023 Sérgio Lopes
 * https:www.sergiolopes.eu, knitter.is@gmail.com
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eu.sergiolopes.codices.repositories;

import eu.sergiolopes.codices.models.Collection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                        rs.getInt("ownedById"), rs.getString("publishDate"),
                        rs.getInt("publishYear"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ObservableList<Collection> findAll() {
        String query = "SELECT * FROM " + tableName + " ORDER BY name";
        ObservableList<Collection> collections = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                collections.add(new Collection(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("ownedById"), rs.getString("publishDate"),
                        rs.getInt("publishYear")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.unmodifiableObservableList(collections);
    }

    @Override
    public List<Collection> findAllForOwner(int ownerId) {
        String query = "SELECT * FROM " + tableName + " WHERE ownedById = " + ownerId + " ORDER BY surname, name";
        ObservableList<Collection> collections = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                collections.add(new Collection(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("ownedById"), rs.getString("publishDate"),
                        rs.getInt("publishYear")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.unmodifiableObservableList(collections);
    }

    @Override
    public ObservableList<Collection> list(int page, int size) {
        String query = "SELECT * FROM " + tableName + " ORDER BY name LIMIT " + size + " OFFSET " + page;
        ObservableList<Collection> collections = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                collections.add(new Collection(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("ownedById"), rs.getString("publishDate"),
                        rs.getInt("publishYear")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.unmodifiableObservableList(collections);
    }

    @Override
    public List<Collection> listForOwner(int ownerId, int page, int size) {
        String query = "SELECT * FROM " + tableName + " WHERE ownedById = " + ownerId + " ORDER BY name LIMIT "
                + size + " OFFSET " + page;

        ObservableList<Collection> collections = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                collections.add(new Collection(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("ownedById"), rs.getString("publishDate"),
                        rs.getInt("publishYear")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.unmodifiableObservableList(collections);
    }

    @Override
    public boolean save(Collection obj) {
        if (obj.getId() > 0) {
            return this.insert(obj);
        }

        return this.update(obj);
    }

    public boolean insert(Collection obj) {
        if (obj.getId() > 0) {
            return false;
        }

        String insertQry = "INSERT INTO " + tableName + "(name, ownedById, publishDate, publishYear) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(insertQry, new String[]{"id"});
            statement.setString(1, obj.getName());
            statement.setInt(2, obj.getOwnedById());
            statement.setString(3, obj.getPublishDate());
            statement.setInt(4, obj.getPublishYear());

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

        String updateQry = "UPDATE " + tableName + " SET name = ?, publishDate = ?, publishYear = ? WHERE id = " + obj.getId();
        try {
            PreparedStatement statement = connection.prepareStatement(updateQry);
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getPublishDate());
            statement.setInt(3, obj.getPublishYear());
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
