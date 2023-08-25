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

import eu.sergiolopes.codices.models.Author;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorRepository implements Repository<Author> {

    private Connection connection;
    private String tableName = "author";

    public AuthorRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Author find(int id) {
        return null;
    }

    @Override
    public ObservableList<Author> findAll() {
        String query = "SELECT * FROM " + tableName;
        ObservableList<Author> authors = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                authors.add(new Author(rs.getInt("id"), rs.getString("name"),
                        rs.getInt("ownedById"), rs.getString("surname"),
                        rs.getString("biography"), rs.getString("website"),
                        rs.getString("photo")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.unmodifiableObservableList(authors);
    }

    @Override
    public ObservableList<Author> list(int page, int offset) {
        return null;
    }

    @Override
    public boolean save(Author obj) {
        return false;
    }

    public boolean create(Author obj) {
        /*public static long create(String tableName, String[] columns, Object[] values, int[] types) {
            int number = Math.min(Math.min(columns.length, values.length), types.length);
            StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + tableName + " (");
            for (int i = 0; i < number; i++) {
                queryBuilder.append(columns[i]);
                if (i < number - 1) queryBuilder.append(", ");
            }
            queryBuilder.append(") ");
            queryBuilder.append(" VALUES (");
            for (int i = 0; i < number; i++) {
                switch (types[i]) {
                    case Types.VARCHAR:
                        queryBuilder.append("'");
                        queryBuilder.append((String) values[i]);
                        queryBuilder.append("'");
                        break;
                    case Types.INTEGER:
                        queryBuilder.append((int) values[i]);
                }
                if (i < number - 1) queryBuilder.append(", ");
            }
            queryBuilder.append(");");
            try (Connection conn = Database.connect()) {
                PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString());
                int affectedRows = pstmt.executeUpdate();
                // check the affected rows
                if (affectedRows > 0) {
                    // get the ID back
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            return rs.getLong(1);
                        }
                    }
                }
            } catch (SQLException ex) {
                Logger.getAnonymousLogger().log(
                        Level.SEVERE,
                        LocalDateTime.now() + ": Could not add person to database");
                return -1;
            }
            return -1;
        }*/
        return false;
    }

    public boolean update(Author obj) {
        /*public static long update(String tableName, String[] columns, Object[] values, int[] types,
                                  String indexFieldName, int indexDataType, Object index) {
            int number = Math.min(Math.min(columns.length, values.length), types.length);
            StringBuilder queryBuilder = new StringBuilder("UPDATE " + tableName + " SET ");
            for (int i = 0; i < number; i++) {
                queryBuilder.append(columns[i]);
                queryBuilder.append(" = ");
                queryBuilder.append(convertObjectToSQLField(values[i], types[i]));
                if (i < number - 1) queryBuilder.append(", ");
            }
            queryBuilder.append(" WHERE ");
            queryBuilder.append(indexFieldName);
            queryBuilder.append(" = ");
            queryBuilder.append(convertObjectToSQLField(index, indexDataType));
            try (Connection conn = Database.connect()) {
                PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString());
                return pstmt.executeUpdate(); //number of affected rows
            } catch (SQLException ex) {
                Logger.getAnonymousLogger().log(
                        Level.SEVERE,
                        LocalDateTime.now() + ": Could not add person to database");
                return -1;
            }
        }*/
        return false;
    }

    @Override
    public boolean delete(Author obj) {
        /* String sql = "DELETE FROM " + tableName + " WHERE id = ?";
            try (Connection conn = Database.connect()) {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                return pstmt.executeUpdate();
            } catch (SQLException e) {
                Logger.getAnonymousLogger().log(
                        Level.SEVERE,
                        LocalDateTime.now() + ": Could not delete from " + tableName + " by id " + id +
                                " because " + e.getCause());
                return -1;
            }*/

        return false;
    }

    /*package com.edencoding.dao;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
    public class CRUDHelper {
        public static Object read(String tableName, String fieldName, int fieldDataType,
                                  String indexFieldName, int indexDataType, Object index) {
            StringBuilder queryBuilder = new StringBuilder("Select ");
            queryBuilder.append(fieldName);
            queryBuilder.append(" from ");
            queryBuilder.append(tableName);
            queryBuilder.append(" where ");
            queryBuilder.append(indexFieldName);
            queryBuilder.append(" = ");
            queryBuilder.append(convertObjectToSQLField(index, indexDataType));
            try (Connection connection = Database.connect()) {
                PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());
                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();
                    switch (fieldDataType) {
                        case Types.INTEGER:
                            return rs.getInt(fieldName);
                        case Types.VARCHAR:
                            return rs.getString(fieldName);
                        default:
                            throw new IllegalArgumentException("Index type " + indexDataType + " from sql.Types is not yet supported.");
                    }
                }
            } catch (SQLException exception) {
                Logger.getAnonymousLogger().log(
                        Level.SEVERE,
                        LocalDateTime.now() + ": Could not fetch from " + tableName + " by index " + index +
                                " and column " + fieldName);
                return null;
            }
        }


        private static String convertObjectToSQLField(Object value, int type) {
            StringBuilder queryBuilder = new StringBuilder();
            switch (type) {
                case Types.VARCHAR:
                    queryBuilder.append("'");
                    queryBuilder.append(value);
                    queryBuilder.append("'");
                    break;
                case Types.INTEGER:
                    queryBuilder.append(value);
                    break;
                default:
                    throw new IllegalArgumentException("Index type " + type + " from sql.Types is not yet supported.");
            }
            return queryBuilder.toString();
        }
    }*/
}
