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

import eu.sergiolopes.codices.models.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ItemRepository implements Repository<Item> {

    private Connection connection;
    private String tableName = "item";

    public ItemRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Item find(int id) {
        String query = "SELECT * FROM " + tableName + " WHERE id = " + id;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                //TODO: Related data (account, series, collection, authors, publisher)
                return new Item(rs.getInt("id"), rs.getString("title"), rs.getString("type"),
                        rs.getInt("translated") == 1, rs.getInt("favorite") == 1,
                        rs.getInt("read") == 1, rs.getInt("copies"), rs.getString("subtitle"),
                        rs.getString("originalTitle"), rs.getString("plot"), rs.getString("isbn"),
                        rs.getString("format"), rs.getInt("pageCount"), rs.getInt("publishYear"),
                        rs.getString("language"), rs.getString("edition"), rs.getFloat("rating"),
                        rs.getFloat("ownRating"), rs.getString("url"), rs.getString("review"),
                        rs.getString("cover"), rs.getString("filename"), rs.getString("narrator"),
                        rs.getString("boughtFrom"), rs.getInt("duration"), rs.getInt("orderInSeries")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ObservableList<Item> findAll() {
        String query = "SELECT * FROM " + tableName + " ORDER BY title";
        ObservableList<Item> items = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                items.add(new Item(rs.getInt("id"), rs.getString("title"), rs.getString("type"),
                        rs.getInt("translated") == 1, rs.getInt("favorite") == 1,
                        rs.getInt("read") == 1, rs.getInt("copies"), rs.getString("subtitle"),
                        rs.getString("originalTitle"), rs.getString("plot"), rs.getString("isbn"),
                        rs.getString("format"), rs.getInt("pageCount"), rs.getInt("publishYear"),
                        rs.getString("language"), rs.getString("edition"), rs.getFloat("rating"),
                        rs.getFloat("ownRating"), rs.getString("url"), rs.getString("review"),
                        rs.getString("cover"), rs.getString("filename"), rs.getString("narrator"),
                        rs.getString("boughtFrom"), rs.getInt("duration"), rs.getInt("orderInSeries")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.unmodifiableObservableList(items);
    }

    @Override
    public List<Item> findAllForOwner(int ownerId) {
        String query = "SELECT * FROM " + tableName + " WHERE ownedById = " + ownerId + " ORDER BY title";
        ObservableList<Item> items = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                items.add(new Item(rs.getInt("id"), rs.getString("title"), rs.getString("type"),
                        rs.getInt("translated") == 1, rs.getInt("favorite") == 1,
                        rs.getInt("read") == 1, rs.getInt("copies"), rs.getString("subtitle"),
                        rs.getString("originalTitle"), rs.getString("plot"), rs.getString("isbn"),
                        rs.getString("format"), rs.getInt("pageCount"), rs.getInt("publishYear"),
                        rs.getString("language"), rs.getString("edition"), rs.getFloat("rating"),
                        rs.getFloat("ownRating"), rs.getString("url"), rs.getString("review"),
                        rs.getString("cover"), rs.getString("filename"), rs.getString("narrator"),
                        rs.getString("boughtFrom"), rs.getInt("duration"), rs.getInt("orderInSeries")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.unmodifiableObservableList(items);
    }

    @Override
    public ObservableList<Item> list(int page, int size) {
        String query = "SELECT * FROM " + tableName + " ORDER BY title LIMIT " + size + " OFFSET " + page;
        ObservableList<Item> items = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                items.add(new Item(rs.getInt("id"), rs.getString("title"), rs.getString("type"),
                        rs.getInt("translated") == 1, rs.getInt("favorite") == 1,
                        rs.getInt("read") == 1, rs.getInt("copies"), rs.getString("subtitle"),
                        rs.getString("originalTitle"), rs.getString("plot"), rs.getString("isbn"),
                        rs.getString("format"), rs.getInt("pageCount"), rs.getInt("publishYear"),
                        rs.getString("language"), rs.getString("edition"), rs.getFloat("rating"),
                        rs.getFloat("ownRating"), rs.getString("url"), rs.getString("review"),
                        rs.getString("cover"), rs.getString("filename"), rs.getString("narrator"),
                        rs.getString("boughtFrom"), rs.getInt("duration"), rs.getInt("orderInSeries")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.unmodifiableObservableList(items);
    }

    @Override
    public List<Item> listForOwner(int ownerId, int page, int size) {
        String query = "SELECT * FROM " + tableName + " WHERE ownedById = " + ownerId + " ORDER BY title LIMIT "
                + size + " OFFSET " + page;

        ObservableList<Item> items = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                items.add(new Item(rs.getInt("id"), rs.getString("title"), rs.getString("type"),
                        rs.getInt("translated") == 1, rs.getInt("favorite") == 1,
                        rs.getInt("read") == 1, rs.getInt("copies"), rs.getString("subtitle"),
                        rs.getString("originalTitle"), rs.getString("plot"), rs.getString("isbn"),
                        rs.getString("format"), rs.getInt("pageCount"), rs.getInt("publishYear"),
                        rs.getString("language"), rs.getString("edition"), rs.getFloat("rating"),
                        rs.getFloat("ownRating"), rs.getString("url"), rs.getString("review"),
                        rs.getString("cover"), rs.getString("filename"), rs.getString("narrator"),
                        rs.getString("boughtFrom"), rs.getInt("duration"), rs.getInt("orderInSeries")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.unmodifiableObservableList(items);
    }

    @Override
    public boolean save(Item obj) {
        if (obj.getId() > 0) {
            return this.insert(obj);
        }

        return this.update(obj);
    }

    public boolean insert(Item obj) {
        if (obj.getId() <= 0) {
            return false;
        }

        //TODO: Save photo file/path/binary/whatever
        String insertQry = "INSERT INTO " + tableName + "(title, ownedById, type, translated, favorite, read, copies, "
                + "subtitle, originalTitle, plot, isbn, format, pageCount, publishDate, publishYear, addedOn, language, "
                + "edition, rating, ownRating, url, review, cover, filename, narrator, boughtFrom, duration, orderInSeries, "
                + "publisherId, seriesId, collectionId, duplicatesId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, null, ?, ?, ?, ?, ?, null, null, null, null)";
        try {
            PreparedStatement statement = connection.prepareStatement(insertQry, new String[]{"id"});

            statement.setString(1, obj.getTitle());
            statement.setInt(2, 1);
            statement.setString(3, obj.getType());
            statement.setInt(4, obj.isTranslated() ? 1 : 0);
            statement.setInt(5, obj.isFavorite() ? 1 : 0);
            statement.setInt(6, obj.isRead() ? 1 : 0);
            statement.setInt(7, obj.getCopies());
            statement.setString(8, obj.getSubtitle());
            statement.setString(9, obj.getOriginalTitle());
            statement.setString(10, obj.getPlot());
            statement.setString(11, obj.getIsbn());
            statement.setString(12, obj.getFormat());
            statement.setInt(13, obj.getPageCount());
            statement.setString(14, obj.getPublishDate());
            statement.setInt(15, obj.getPublishYear());
            statement.setString(16, obj.getLanguage());
            statement.setString(17, obj.getEdition());
            statement.setFloat(18, obj.getRating());
            statement.setFloat(19, obj.getOwnRating());
            statement.setString(20, obj.getUrl());
            statement.setString(21, obj.getReview());
            statement.setString(22, obj.getFilename());
            statement.setString(23, obj.getNarrator());
            statement.setString(24, obj.getBoughtFrom());
            statement.setInt(25, obj.getDuration());
            statement.setInt(26, obj.getOrderInSeries());

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

    public boolean update(Item obj) {
        if (obj.getId() <= 0) {
            return false;
        }

        //TODO: Update photo field
        String updateQry = "UPDATE " + tableName + " SET title = ?, translated = ?, favorite = ?, read = ?, copies = ?, "
                + "subtitle = ?, originalTitle = ?, plot = ?, isbn = ?, format = ?, pageCount = ?, publishDate = ?, "
                + "publishYear = ?, language = ?, edition = ?, rating = ?, ownRating = ?, url = ?, review = ?, filename = ?, "
                + "narrator = ?, boughtFrom = ?, duration = ?, orderInSeries = ? WHERE id = " + obj.getId();
        try {
            PreparedStatement statement = connection.prepareStatement(updateQry);
            statement.setString(1, obj.getTitle());
            statement.setInt(2, obj.isTranslated() ? 1 : 0);
            statement.setInt(3, obj.isFavorite() ? 1 : 0);
            statement.setInt(4, obj.isRead() ? 1 : 0);
            statement.setInt(5, obj.getCopies());
            statement.setString(6, obj.getSubtitle());
            statement.setString(7, obj.getOriginalTitle());
            statement.setString(8, obj.getPlot());
            statement.setString(9, obj.getIsbn());
            statement.setString(10, obj.getFormat());
            statement.setInt(11, obj.getPageCount());
            statement.setString(12, obj.getPublishDate());
            statement.setInt(13, obj.getPublishYear());
            statement.setString(14, obj.getLanguage());
            statement.setString(15, obj.getEdition());
            statement.setFloat(16, obj.getRating());
            statement.setFloat(17, obj.getOwnRating());
            statement.setString(18, obj.getUrl());
            statement.setString(19, obj.getReview());
            statement.setString(20, obj.getFilename());
            statement.setString(21, obj.getNarrator());
            statement.setString(22, obj.getBoughtFrom());
            statement.setInt(23, obj.getDuration());
            statement.setInt(24, obj.getOrderInSeries());

            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Item obj) {
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

    public ObservableList<Item> findAllPaperBooks() {
        return this.findAllByType("paper");
    }

    public ObservableList<Item> findAllEbooks() {
        return this.findAllByType("ebook");
    }

    public ObservableList<Item> findAllAudioBooks() {
        return this.findAllByType("audio");

    }

    private ObservableList<Item> findAllByType(String type) {
        //TODO: Make type into ENUM
        ObservableList<Item> items = FXCollections.observableArrayList();
        if (type != "paper" && type != "ebook" && type != "audio") {
            return FXCollections.unmodifiableObservableList(items);
        }

        String query = "SELECT * FROM " + tableName + " WHERE type = ?  ORDER BY title";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, type);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                items.add(new Item(rs.getInt("id"), rs.getString("title"), rs.getString("type"),
                        rs.getInt("translated") == 1, rs.getInt("favorite") == 1,
                        rs.getInt("read") == 1, rs.getInt("copies"), rs.getString("subtitle"),
                        rs.getString("originalTitle"), rs.getString("plot"), rs.getString("isbn"),
                        rs.getString("format"), rs.getInt("pageCount"), rs.getInt("publishYear"),
                        rs.getString("language"), rs.getString("edition"), rs.getFloat("rating"),
                        rs.getFloat("ownRating"), rs.getString("url"), rs.getString("review"),
                        rs.getString("cover"), rs.getString("filename"), rs.getString("narrator"),
                        rs.getString("boughtFrom"), rs.getInt("duration"), rs.getInt("orderInSeries")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.unmodifiableObservableList(items);
    }
//    @Override
//    public ObservableList<Item> findAll() {
//        String query = "SELECT * FROM " + tableName;
//        ObservableList<Item> items = FXCollections.observableArrayList();
//        try {
//            PreparedStatement statement = connection.prepareStatement(query);
//            ResultSet rs = statement.executeQuery();
//            while (rs.next()) {
//
////                items.add();
//            }
//
//            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//    String ts = sdf.format(timestamp);*/
//        } catch (SQLException e) {
////            Logger.getAnonymousLogger().log(
////                    Level.SEVERE,
////                    LocalDateTime.now() + ": Could not load Persons from database ");
////            persons.clear();
//        }
//
//        return FXCollections.unmodifiableObservableList(items);
//    }

}
