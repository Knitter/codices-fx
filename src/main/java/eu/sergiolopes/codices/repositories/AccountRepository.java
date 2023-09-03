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

import eu.sergiolopes.codices.models.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository implements Repository<Account> {

    private Connection connection;
    private String tableName = "account";

    public AccountRepository(Connection connection) {
        this.connection = connection;
    }

    public Account find(int id) {
        String query = "SELECT * FROM " + tableName + " WHERE id = " + id;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("id"), rs.getString("username"),
                        rs.getString("email"), rs.getString("name"),
                        rs.getInt("active") == 1, null);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ObservableList<Account> findAll() {
        String query = "SELECT * FROM " + tableName + " ORDER BY name, username";
        ObservableList<Account> accounts = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                accounts.add(new Account(rs.getInt("id"), rs.getString("username"),
                        rs.getString("email"), rs.getString("name"),
                        rs.getInt("active") == 1, null));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.unmodifiableObservableList(accounts);
    }

    @Override
    public List<Account> findAllForOwner(int ownerId) {
        return new ArrayList<>();
    }

    @Override
    public ObservableList<Account> list(int page, int size) {
        String query = "SELECT * FROM " + tableName + " ORDER BY name, username LIMIT " + size + " OFFSET " + page;
        ObservableList<Account> accounts = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                accounts.add(new Account(rs.getInt("id"), rs.getString("username"),
                        rs.getString("email"), rs.getString("name"),
                        rs.getInt("active") == 1, null));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.unmodifiableObservableList(accounts);
    }

    @Override
    public List<Account> listForOwner(int ownerId, int page, int size) {
        return new ArrayList<>();
    }

    @Override
    public boolean save(Account obj) {
        if (obj.getId() <= 0) {
            return this.insert(obj);
        }

        return this.update(obj);
    }

    public boolean insert(Account obj) {
        if (obj.getId() > 0) {
            return false;
        }

        String insertQry = "INSERT INTO " + tableName + "(username, email, name, active, password, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try {
            PreparedStatement statement = connection.prepareStatement(insertQry, new String[]{"id"});
            statement.setString(1, obj.getUsername());
            statement.setString(2, obj.getEmail());
            statement.setString(3, obj.getName());
            statement.setInt(4, obj.isActive() ? 1 : 0);

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

    public boolean update(Account obj) {
        if (obj.getId() <= 0) {
            return false;
        }

        String updateQry = "UPDATE " + tableName + " SET username = ?, email = ?, name = ?, active = ?, updatedAt = CURRENT_TIMESTAMP WHERE id = " + obj.getId();
        try {
            PreparedStatement statement = connection.prepareStatement(updateQry);
            statement.setString(1, obj.getUsername());
            statement.setString(2, obj.getEmail());
            statement.setString(3, obj.getName());
            statement.setInt(4, obj.isActive() ? 1 : 0);
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Account obj) {
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
