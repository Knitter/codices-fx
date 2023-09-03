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

import eu.sergiolopes.codices.models.Series;
import eu.sergiolopes.codices.models.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeriesRepository implements Repository<Series> {

    private Connection connection;
    private String tableName = "series";

    public SeriesRepository(Connection connection) {
        this.connection = connection;
    }

    public Series find(int id) {
        String query = "SELECT * FROM " + tableName + " WHERE id = " + id;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Series(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("completed") == 1, rs.getInt("bookCount"),
                        rs.getInt("ownedCount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ObservableList<Series> findAll() {
        String query = "SELECT * FROM " + tableName + " ORDER BY name";
        List<Series> series = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                series.add(new Series(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("completed") == 1, rs.getInt("bookCount"),
                        rs.getInt("ownedCount")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(series);
    }

    @Override
    public List<Series> findAllForOwner(int ownerId) {
        String query = "SELECT * FROM " + tableName + " WHERE ownedById = " + ownerId + " ORDER BY name";
        List<Series> series = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                series.add(new Series(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("completed") == 1, rs.getInt("bookCount"),
                        rs.getInt("ownedCount")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(series);
    }

    @Override
    public ObservableList<Series> list(int page, int size) {
        String query = "SELECT * FROM " + tableName + " ORDER BY name LIMIT " + size + " OFFSET " + page;
        List<Series> series = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                series.add(new Series(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("completed") == 1, rs.getInt("bookCount"),
                        rs.getInt("ownedCount")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(series);
    }

    @Override
    public List<Series> listForOwner(int ownerId, int page, int size) {
        String query = "SELECT * FROM " + tableName + " WHERE ownedById = " + ownerId + " ORDER BY name LIMIT "
                + size + " OFFSET " + page;

        List<Series> series = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                series.add(new Series(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("completed") == 1, rs.getInt("bookCount"),
                        rs.getInt("ownedCount")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(series);
    }

    @Override
    public boolean save(Series obj) {
        if (obj.getId() <= 0) {
            return this.insert(obj);
        }

        return this.update(obj);
    }

    @Override
    public boolean insert(Series obj) {
        if (obj.getId() > 0) {
            return false;
        }

        String insertQry = "INSERT INTO " + tableName + "(name, ownedById, completed, bookCount, ownedCount) VALUES (?, 1, ?, ?, 0)";
        try {
            PreparedStatement statement = connection.prepareStatement(insertQry, new String[]{"id"});
            statement.setString(1, obj.getName());
            statement.setInt(2, obj.isCompleted() ? 1 : 0);
            statement.setInt(3, obj.getBookCount());

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
    public boolean update(Series obj) {
        if (obj.getId() <= 0) {
            return false;
        }

        String updateQry = "UPDATE " + tableName + " SET name = ?, completed = ?, bookCount = ? WHERE id = " + obj.getId();
        try {
            PreparedStatement statement = connection.prepareStatement(updateQry);
            statement.setString(1, obj.getName());
            statement.setInt(2, obj.isCompleted() ? 1 : 0);
            statement.setInt(3, obj.getBookCount());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Series obj) {
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

    /**
     * @param seriesId
     * @return
     */
    public boolean incrementOwnedBookCount(int seriesId) {
        if (seriesId <= 0) {
            return false;
        }

        String updateQry = "UPDATE " + tableName + " SET ownedCount = (ownedCount + 1) WHERE id = " + seriesId;
        try {
            PreparedStatement statement = connection.prepareStatement(updateQry);
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
