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

import eu.sergiolopes.codices.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository implements Repository<Item> {

    private static final String relatedAuthorsQry = "SELECT a.* FROM author AS a INNER JOIN item_author AS ia ON a.id = ia.authorId WHERE itemId = ? ORDER BY surname, name";
    private static final String relatedGenresQry = "SELECT g.* FROM genre AS g INNER JOIN item_genre AS ig ON g.id = ig.genreId WHERE itemId = ? ORDER BY g.name";
    private static final String relatedPublisherQry = "SELECT * FROM publisher WHERE id = ?";
    private static final String relatedCollectionQry = "SELECT * FROM collection WHERE id = ?";
    private static final String relatedSeriesQry = "SELECT * FROM series WHERE id = ?";
    private Connection connection;
    private String tableName = "item";

    public ItemRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Item find(int id) {
        String itemQry = "SELECT * FROM " + tableName + " WHERE id = " + id;
        try {
            PreparedStatement itemStatement = connection.prepareStatement(itemQry);
            ResultSet itemRs = itemStatement.executeQuery();
            if (itemRs.next()) {
                Item item = new Item(itemRs.getInt("id"), itemRs.getString("title"),
                        ItemType.fromString(itemRs.getString("type")),
                        itemRs.getInt("translated") == 1, itemRs.getInt("read") == 1,
                        itemRs.getInt("copies"), itemRs.getString("subtitle"),
                        itemRs.getString("originalTitle"), itemRs.getString("plot"),
                        itemRs.getString("isbn"), itemRs.getString("format"),
                        itemRs.getInt("pageCount"), itemRs.getString("publishDate"),
                        itemRs.getInt("publishYear"), itemRs.getString("addedOn"),
                        itemRs.getString("language"), itemRs.getString("edition"),
                        itemRs.getString("volume"), itemRs.getFloat("rating"),
                        itemRs.getString("url"), itemRs.getString("review"),
                        itemRs.getString("cover"), itemRs.getString("filename"),
                        itemRs.getString("fileLocation"), itemRs.getString("narrator"),
                        itemRs.getString("bitrate"), itemRs.getString("boughtFrom"),
                        itemRs.getInt("sizeBytes"), itemRs.getInt("duration"),
                        itemRs.getInt("orderInSeries"));

                int relId = itemRs.getInt("publisherId");
                if (relId > 0) {
                    PreparedStatement pubStatement = connection.prepareStatement(relatedPublisherQry);
                    pubStatement.setInt(1, id);
                    ResultSet pubRs = pubStatement.executeQuery();
                    if (pubRs.next()) {
                        item.setPublisher(new Publisher(pubRs.getInt("id"), pubRs.getString("name"),
                                pubRs.getString("summary"), pubRs.getString("website")));
                    }
                }

                relId = itemRs.getInt("seriesId");
                if (relId > 0) {
                    PreparedStatement seriesStatement = connection.prepareStatement(relatedSeriesQry);
                    seriesStatement.setInt(1, id);
                    ResultSet seriesRs = seriesStatement.executeQuery();
                    if (seriesRs.next()) {
                        item.setSeries(new Series(seriesRs.getInt("id"), seriesRs.getString("name"),
                                seriesRs.getInt("completed") == 1, seriesRs.getInt("bookCount"),
                                seriesRs.getInt("ownedCount")));
                    }
                }

                relId = itemRs.getInt("collectionId");
                if (relId > 0) {
                    PreparedStatement colStatement = connection.prepareStatement(relatedCollectionQry);
                    colStatement.setInt(1, id);
                    ResultSet colRs = colStatement.executeQuery();
                    if (colRs.next()) {
                        item.setCollection(new Collection(colRs.getInt("id"), colRs.getString("name"),
                                colRs.getString("publishDate"), colRs.getInt("publishYear"),
                                colRs.getString("description")));
                    }
                }

                //relId = rs.getInt("duplicatesId");
                //if(relId > 0) {
                //PreparedStatement relStatement = connection.prepareStatement();
                //ResultSet relRs = statement.executeQuery();
                //if (relRs.next()) {
                //TODO ...
                //}

                List<Author> authors = new ArrayList<>();
                PreparedStatement authorsStatement = connection.prepareStatement(relatedAuthorsQry);
                authorsStatement.setInt(1, id);
                ResultSet authorsRs = authorsStatement.executeQuery();
                while (authorsRs.next()) {
                    authors.add(new Author(authorsRs.getInt("id"), authorsRs.getString("name"),
                            authorsRs.getString("surname"), authorsRs.getString("biography"),
                            authorsRs.getString("website")));
                }
                item.setAuthors(authors);

                List<Genre> genres = new ArrayList<>();
                PreparedStatement genresStatement = connection.prepareStatement(relatedGenresQry);
                genresStatement.setInt(1, id);
                ResultSet genresRs = genresStatement.executeQuery();
                while (genresRs.next()) {
                    genres.add(new Genre(genresRs.getInt("id"), genresRs.getString("name")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ObservableList<Item> findAll() {
        String itemQry = "SELECT * FROM " + tableName + " ORDER BY title";
        List<Item> items = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();
        try {
            PreparedStatement itemStatement = connection.prepareStatement(itemQry);
            ResultSet itemRs = itemStatement.executeQuery();

            Item item;
            while (itemRs.next()) {
                item = new Item(itemRs.getInt("id"), itemRs.getString("title"),
                        ItemType.fromString(itemRs.getString("type")),
                        itemRs.getInt("translated") == 1, itemRs.getInt("read") == 1,
                        itemRs.getInt("copies"), itemRs.getString("subtitle"),
                        itemRs.getString("originalTitle"), itemRs.getString("plot"),
                        itemRs.getString("isbn"), itemRs.getString("format"),
                        itemRs.getInt("pageCount"), itemRs.getString("publishDate"),
                        itemRs.getInt("publishYear"), itemRs.getString("addedOn"),
                        itemRs.getString("language"), itemRs.getString("edition"),
                        itemRs.getString("volume"), itemRs.getFloat("rating"),
                        itemRs.getString("url"), itemRs.getString("review"),
                        itemRs.getString("cover"), itemRs.getString("filename"),
                        itemRs.getString("fileLocation"), itemRs.getString("narrator"),
                        itemRs.getString("bitrate"), itemRs.getString("boughtFrom"),
                        itemRs.getInt("sizeBytes"), itemRs.getInt("duration"),
                        itemRs.getInt("orderInSeries"));

                int relId = itemRs.getInt("publisherId");
                if (relId > 0) {
                    PreparedStatement pubStatement = connection.prepareStatement(relatedPublisherQry);
                    pubStatement.setInt(1, item.getId());
                    ResultSet pubRs = pubStatement.executeQuery();
                    if (pubRs.next()) {
                        item.setPublisher(new Publisher(pubRs.getInt("id"), pubRs.getString("name"),
                                pubRs.getString("summary"), pubRs.getString("website")));
                    }
                }

                relId = itemRs.getInt("seriesId");
                if (relId > 0) {
                    PreparedStatement seriesStatement = connection.prepareStatement(relatedSeriesQry);
                    seriesStatement.setInt(1, item.getId());
                    ResultSet seriesRs = seriesStatement.executeQuery();
                    if (seriesRs.next()) {
                        item.setSeries(new Series(seriesRs.getInt("id"), seriesRs.getString("name"),
                                seriesRs.getInt("completed") == 1, seriesRs.getInt("bookCount"),
                                seriesRs.getInt("ownedCount")));
                    }
                }

                relId = itemRs.getInt("collectionId");
                if (relId > 0) {
                    PreparedStatement colStatement = connection.prepareStatement(relatedCollectionQry);
                    colStatement.setInt(1, item.getId());
                    ResultSet colRs = colStatement.executeQuery();
                    if (colRs.next()) {
                        item.setCollection(new Collection(colRs.getInt("id"), colRs.getString("name"),
                                colRs.getString("publishDate"), colRs.getInt("publishYear"),
                                colRs.getString("description")));
                    }
                }

                //relId = rs.getInt("duplicatesId");
                //if(relId > 0) {
                //PreparedStatement relStatement = connection.prepareStatement();
                //ResultSet relRs = statement.executeQuery();
                //if (relRs.next()) {
                //TODO ...
                //}

                authors.clear();
                PreparedStatement authorsStatement = connection.prepareStatement(relatedAuthorsQry);
                authorsStatement.setInt(1, item.getId());
                ResultSet authorsRs = authorsStatement.executeQuery();
                while (authorsRs.next()) {
                    authors.add(new Author(authorsRs.getInt("id"), authorsRs.getString("name"),
                            authorsRs.getString("surname"), authorsRs.getString("biography"),
                            authorsRs.getString("website")));
                }
                item.setAuthors(authors);

                genres.clear();
                PreparedStatement genresStatement = connection.prepareStatement(relatedGenresQry);
                genresStatement.setInt(1, item.getId());
                ResultSet genresRs = genresStatement.executeQuery();
                while (genresRs.next()) {
                    genres.add(new Genre(genresRs.getInt("id"), genresRs.getString("name")));
                }

                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(items);
    }

    @Override
    public List<Item> findAllForOwner(int ownerId) {
        String itemQry = "SELECT * FROM " + tableName + " WHERE ownedById = " + ownerId + " ORDER BY title";
        List<Item> items = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();
        try {
            PreparedStatement itemStatement = connection.prepareStatement(itemQry);
            ResultSet itemRs = itemStatement.executeQuery();

            Item item;
            while (itemRs.next()) {
                item = new Item(itemRs.getInt("id"), itemRs.getString("title"),
                        ItemType.fromString(itemRs.getString("type")),
                        itemRs.getInt("translated") == 1, itemRs.getInt("read") == 1,
                        itemRs.getInt("copies"), itemRs.getString("subtitle"),
                        itemRs.getString("originalTitle"), itemRs.getString("plot"),
                        itemRs.getString("isbn"), itemRs.getString("format"),
                        itemRs.getInt("pageCount"), itemRs.getString("publishDate"),
                        itemRs.getInt("publishYear"), itemRs.getString("addedOn"),
                        itemRs.getString("language"), itemRs.getString("edition"),
                        itemRs.getString("volume"), itemRs.getFloat("rating"),
                        itemRs.getString("url"), itemRs.getString("review"),
                        itemRs.getString("cover"), itemRs.getString("filename"),
                        itemRs.getString("fileLocation"), itemRs.getString("narrator"),
                        itemRs.getString("bitrate"), itemRs.getString("boughtFrom"),
                        itemRs.getInt("sizeBytes"), itemRs.getInt("duration"),
                        itemRs.getInt("orderInSeries"));

                int relId = itemRs.getInt("publisherId");
                if (relId > 0) {
                    PreparedStatement pubStatement = connection.prepareStatement(relatedPublisherQry);
                    pubStatement.setInt(1, item.getId());
                    ResultSet pubRs = pubStatement.executeQuery();
                    if (pubRs.next()) {
                        item.setPublisher(new Publisher(pubRs.getInt("id"), pubRs.getString("name"),
                                pubRs.getString("summary"), pubRs.getString("website")));
                    }
                }

                relId = itemRs.getInt("seriesId");
                if (relId > 0) {
                    PreparedStatement seriesStatement = connection.prepareStatement(relatedSeriesQry);
                    seriesStatement.setInt(1, item.getId());
                    ResultSet seriesRs = seriesStatement.executeQuery();
                    if (seriesRs.next()) {
                        item.setSeries(new Series(seriesRs.getInt("id"), seriesRs.getString("name"),
                                seriesRs.getInt("completed") == 1, seriesRs.getInt("bookCount"),
                                seriesRs.getInt("ownedCount")));
                    }
                }

                relId = itemRs.getInt("collectionId");
                if (relId > 0) {
                    PreparedStatement colStatement = connection.prepareStatement(relatedCollectionQry);
                    colStatement.setInt(1, item.getId());
                    ResultSet colRs = colStatement.executeQuery();
                    if (colRs.next()) {
                        item.setCollection(new Collection(colRs.getInt("id"), colRs.getString("name"),
                                colRs.getString("publishDate"), colRs.getInt("publishYear"),
                                colRs.getString("description")));
                    }
                }

                //relId = rs.getInt("duplicatesId");
                //if(relId > 0) {
                //PreparedStatement relStatement = connection.prepareStatement();
                //ResultSet relRs = statement.executeQuery();
                //if (relRs.next()) {
                //TODO ...
                //}

                authors.clear();
                PreparedStatement authorsStatement = connection.prepareStatement(relatedAuthorsQry);
                authorsStatement.setInt(1, item.getId());
                ResultSet authorsRs = authorsStatement.executeQuery();
                while (authorsRs.next()) {
                    authors.add(new Author(authorsRs.getInt("id"), authorsRs.getString("name"),
                            authorsRs.getString("surname"), authorsRs.getString("biography"),
                            authorsRs.getString("website")));
                }
                item.setAuthors(authors);

                genres.clear();
                PreparedStatement genresStatement = connection.prepareStatement(relatedGenresQry);
                genresStatement.setInt(1, item.getId());
                ResultSet genresRs = genresStatement.executeQuery();
                while (genresRs.next()) {
                    genres.add(new Genre(genresRs.getInt("id"), genresRs.getString("name")));
                }

                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(items);
    }

    @Override
    public ObservableList<Item> list(int page, int size) {
        String itemQry = "SELECT * FROM " + tableName + " ORDER BY title LIMIT " + size + " OFFSET " + page;
        List<Item> items = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();
        try {
            PreparedStatement itemStatement = connection.prepareStatement(itemQry);
            ResultSet itemRs = itemStatement.executeQuery();

            Item item;
            while (itemRs.next()) {
                item = new Item(itemRs.getInt("id"), itemRs.getString("title"),
                        ItemType.fromString(itemRs.getString("type")),
                        itemRs.getInt("translated") == 1, itemRs.getInt("read") == 1,
                        itemRs.getInt("copies"), itemRs.getString("subtitle"),
                        itemRs.getString("originalTitle"), itemRs.getString("plot"),
                        itemRs.getString("isbn"), itemRs.getString("format"),
                        itemRs.getInt("pageCount"), itemRs.getString("publishDate"),
                        itemRs.getInt("publishYear"), itemRs.getString("addedOn"),
                        itemRs.getString("language"), itemRs.getString("edition"),
                        itemRs.getString("volume"), itemRs.getFloat("rating"),
                        itemRs.getString("url"), itemRs.getString("review"),
                        itemRs.getString("cover"), itemRs.getString("filename"),
                        itemRs.getString("fileLocation"), itemRs.getString("narrator"),
                        itemRs.getString("bitrate"), itemRs.getString("boughtFrom"),
                        itemRs.getInt("sizeBytes"), itemRs.getInt("duration"),
                        itemRs.getInt("orderInSeries"));

                int relId = itemRs.getInt("publisherId");
                if (relId > 0) {
                    PreparedStatement pubStatement = connection.prepareStatement(relatedPublisherQry);
                    pubStatement.setInt(1, item.getId());
                    ResultSet pubRs = pubStatement.executeQuery();
                    if (pubRs.next()) {
                        item.setPublisher(new Publisher(pubRs.getInt("id"), pubRs.getString("name"),
                                pubRs.getString("summary"), pubRs.getString("website")));
                    }
                }

                relId = itemRs.getInt("seriesId");
                if (relId > 0) {
                    PreparedStatement seriesStatement = connection.prepareStatement(relatedSeriesQry);
                    seriesStatement.setInt(1, item.getId());
                    ResultSet seriesRs = seriesStatement.executeQuery();
                    if (seriesRs.next()) {
                        item.setSeries(new Series(seriesRs.getInt("id"), seriesRs.getString("name"),
                                seriesRs.getInt("completed") == 1, seriesRs.getInt("bookCount"),
                                seriesRs.getInt("ownedCount")));
                    }
                }

                relId = itemRs.getInt("collectionId");
                if (relId > 0) {
                    PreparedStatement colStatement = connection.prepareStatement(relatedCollectionQry);
                    colStatement.setInt(1, item.getId());
                    ResultSet colRs = colStatement.executeQuery();
                    if (colRs.next()) {
                        item.setCollection(new Collection(colRs.getInt("id"), colRs.getString("name"),
                                colRs.getString("publishDate"), colRs.getInt("publishYear"),
                                colRs.getString("description")));
                    }
                }

                //relId = rs.getInt("duplicatesId");
                //if(relId > 0) {
                //PreparedStatement relStatement = connection.prepareStatement();
                //ResultSet relRs = statement.executeQuery();
                //if (relRs.next()) {
                //TODO ...
                //}

                authors.clear();
                PreparedStatement authorsStatement = connection.prepareStatement(relatedAuthorsQry);
                authorsStatement.setInt(1, item.getId());
                ResultSet authorsRs = authorsStatement.executeQuery();
                while (authorsRs.next()) {
                    authors.add(new Author(authorsRs.getInt("id"), authorsRs.getString("name"),
                            authorsRs.getString("surname"), authorsRs.getString("biography"),
                            authorsRs.getString("website")));
                }
                item.setAuthors(authors);

                genres.clear();
                PreparedStatement genresStatement = connection.prepareStatement(relatedGenresQry);
                genresStatement.setInt(1, item.getId());
                ResultSet genresRs = genresStatement.executeQuery();
                while (genresRs.next()) {
                    genres.add(new Genre(genresRs.getInt("id"), genresRs.getString("name")));
                }

                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(items);
    }

    @Override
    public List<Item> listForOwner(int ownerId, int page, int size) {
        String itemQry = "SELECT * FROM " + tableName + " WHERE ownedById = " + ownerId + " ORDER BY title LIMIT "
                + size + " OFFSET " + page;

        List<Item> items = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();
        try {
            PreparedStatement itemStatement = connection.prepareStatement(itemQry);
            ResultSet itemRs = itemStatement.executeQuery();

            Item item;
            while (itemRs.next()) {
                item = new Item(itemRs.getInt("id"), itemRs.getString("title"),
                        ItemType.fromString(itemRs.getString("type")),
                        itemRs.getInt("translated") == 1, itemRs.getInt("read") == 1,
                        itemRs.getInt("copies"), itemRs.getString("subtitle"),
                        itemRs.getString("originalTitle"), itemRs.getString("plot"),
                        itemRs.getString("isbn"), itemRs.getString("format"),
                        itemRs.getInt("pageCount"), itemRs.getString("publishDate"),
                        itemRs.getInt("publishYear"), itemRs.getString("addedOn"),
                        itemRs.getString("language"), itemRs.getString("edition"),
                        itemRs.getString("volume"), itemRs.getFloat("rating"),
                        itemRs.getString("url"), itemRs.getString("review"),
                        itemRs.getString("cover"), itemRs.getString("filename"),
                        itemRs.getString("fileLocation"), itemRs.getString("narrator"),
                        itemRs.getString("bitrate"), itemRs.getString("boughtFrom"),
                        itemRs.getInt("sizeBytes"), itemRs.getInt("duration"),
                        itemRs.getInt("orderInSeries"));

                int relId = itemRs.getInt("publisherId");
                if (relId > 0) {
                    PreparedStatement pubStatement = connection.prepareStatement(relatedPublisherQry);
                    pubStatement.setInt(1, item.getId());
                    ResultSet pubRs = pubStatement.executeQuery();
                    if (pubRs.next()) {
                        item.setPublisher(new Publisher(pubRs.getInt("id"), pubRs.getString("name"),
                                pubRs.getString("summary"), pubRs.getString("website")));
                    }
                }

                relId = itemRs.getInt("seriesId");
                if (relId > 0) {
                    PreparedStatement seriesStatement = connection.prepareStatement(relatedSeriesQry);
                    seriesStatement.setInt(1, item.getId());
                    ResultSet seriesRs = seriesStatement.executeQuery();
                    if (seriesRs.next()) {
                        item.setSeries(new Series(seriesRs.getInt("id"), seriesRs.getString("name"),
                                seriesRs.getInt("completed") == 1, seriesRs.getInt("bookCount"),
                                seriesRs.getInt("ownedCount")));
                    }
                }

                relId = itemRs.getInt("collectionId");
                if (relId > 0) {
                    PreparedStatement colStatement = connection.prepareStatement(relatedCollectionQry);
                    colStatement.setInt(1, item.getId());
                    ResultSet colRs = colStatement.executeQuery();
                    if (colRs.next()) {
                        item.setCollection(new Collection(colRs.getInt("id"), colRs.getString("name"),
                                colRs.getString("publishDate"), colRs.getInt("publishYear"),
                                colRs.getString("description")));
                    }
                }

                //relId = rs.getInt("duplicatesId");
                //if(relId > 0) {
                //PreparedStatement relStatement = connection.prepareStatement();
                //ResultSet relRs = statement.executeQuery();
                //if (relRs.next()) {
                //TODO ...
                //}

                authors.clear();
                PreparedStatement authorsStatement = connection.prepareStatement(relatedAuthorsQry);
                authorsStatement.setInt(1, item.getId());
                ResultSet authorsRs = authorsStatement.executeQuery();
                while (authorsRs.next()) {
                    authors.add(new Author(authorsRs.getInt("id"), authorsRs.getString("name"),
                            authorsRs.getString("surname"), authorsRs.getString("biography"),
                            authorsRs.getString("website")));
                }
                item.setAuthors(authors);

                genres.clear();
                PreparedStatement genresStatement = connection.prepareStatement(relatedGenresQry);
                genresStatement.setInt(1, item.getId());
                ResultSet genresRs = genresStatement.executeQuery();
                while (genresRs.next()) {
                    genres.add(new Genre(genresRs.getInt("id"), genresRs.getString("name")));
                }

                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(items);
    }

    @Override
    public boolean save(Item obj) {
        if (obj.getId() <= 0) {
            return this.insert(obj);
        }

        return this.update(obj);
    }

    public boolean insert(Item obj) {
        if (obj.getId() > 0) {
            return false;
        }

        String insertQry = "INSERT INTO " + tableName + "(title, ownedById, type, translated, read,copies, subtitle, "
                + "originalTitle, plot,isbn, format,pageCount,  publishDate, publishYear, addedOn, language, edition, "
                + "volume, rating, url, review, cover, filename, fileLocation, narrator, bitrate, boughtFrom, duration, "
                + "sizeBytes, orderInSeries, publisherId, seriesId, collectionId, duplicatesId) VALUES (?, 1, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NULL)";
        try {
            PreparedStatement statement = connection.prepareStatement(insertQry, new String[]{"id"});

            statement.setString(1, obj.getTitle());
            statement.setString(2, obj.getType().getType());
            statement.setInt(3, obj.isTranslated() ? 1 : 0);
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
            statement.setString(16, obj.getVolume());
            statement.setFloat(17, obj.getRating());
            statement.setString(18, obj.getUrl());
            statement.setString(19, obj.getReview());
            statement.setString(20, obj.getFilename());
            statement.setString(21, obj.getFileLocation());
            statement.setString(22, obj.getNarrator());
            statement.setString(23, obj.getBoughtFrom());
            statement.setInt(24, obj.getDuration());
            statement.setInt(25, obj.getSizeBytes());
            statement.setInt(26, obj.getOrderInSeries());

            if (obj.getPublisher() != null) {
                statement.setInt(27, obj.getPublisher().getId());
            } else {
                statement.setNull(27, Types.INTEGER);
            }

            if (obj.getSeries() != null) {
                statement.setInt(28, obj.getSeries().getId());
            } else {
                statement.setNull(28, Types.INTEGER);
            }

            if (obj.getCollection() != null) {
                statement.setInt(29, obj.getCollection().getId());
            } else {
                statement.setNull(29, Types.INTEGER);
            }

            if (statement.executeUpdate() <= 0) {
                return false;
            }

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                obj.setId(keys.getInt("id"));
            }
            //TODO: authors + genres

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

        String updateQry = "UPDATE " + tableName + " SET title = ?, type = ?, translated = ?, read = ?, copies = ?, subtitle = ?, "
                + "originalTitle = ?, plot = ?, isbn = ?, format = ?, pageCount = ?, publishDate = ?, publishYear = ?, "
                + "language = ?, edition = ?, volume = ?, rating = ?, url = ?, review = ?, filename = ?, fileLocation = ?, "
                + "narrator = ?, bitrate = ?, boughtFrom = ?, duration = ?, sizeBytes = ?, orderInSeries = ?, publisherId = ?,"
                + "seriesId = ?,  collectionId = ? WHERE id = " + obj.getId();
        try {
            PreparedStatement statement = connection.prepareStatement(updateQry);
            statement.setString(1, obj.getTitle());
            statement.setString(2, obj.getType().getType());
            statement.setInt(3, obj.isTranslated() ? 1 : 0);
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
            statement.setString(16, obj.getVolume());
            statement.setFloat(17, obj.getRating());
            statement.setString(18, obj.getUrl());
            statement.setString(19, obj.getReview());
            statement.setString(20, obj.getFilename());
            statement.setString(21, obj.getFileLocation());
            statement.setString(22, obj.getNarrator());
            statement.setString(23, obj.getBoughtFrom());
            statement.setInt(24, obj.getDuration());
            statement.setInt(25, obj.getSizeBytes());
            statement.setInt(26, obj.getOrderInSeries());

            if (obj.getPublisher() != null) {
                statement.setInt(27, obj.getPublisher().getId());
            } else {
                statement.setNull(27, Types.INTEGER);
            }

            if (obj.getSeries() != null) {
                statement.setInt(28, obj.getSeries().getId());
            } else {
                statement.setNull(28, Types.INTEGER);
            }

            if (obj.getCollection() != null) {
                statement.setInt(29, obj.getCollection().getId());
            } else {
                statement.setNull(29, Types.INTEGER);
            }

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
        List<Item> items = new ArrayList<>();
        //TODO: ...
        if (type != "paper" && type != "ebook" && type != "audio") {
            return FXCollections.observableList(items);
        }

        String itemQry = "SELECT * FROM " + tableName + " WHERE type = ?  ORDER BY title";
        try {
            PreparedStatement itemStatement = connection.prepareStatement(itemQry);
            itemStatement.setString(1, type);
            ResultSet itemRs = itemStatement.executeQuery();

            Item item;
            while (itemRs.next()) {
                item = new Item(itemRs.getInt("id"), itemRs.getString("title"),
                        ItemType.fromString(itemRs.getString("type")),
                        itemRs.getInt("translated") == 1, itemRs.getInt("read") == 1,
                        itemRs.getInt("copies"), itemRs.getString("subtitle"),
                        itemRs.getString("originalTitle"), itemRs.getString("plot"),
                        itemRs.getString("isbn"), itemRs.getString("format"),
                        itemRs.getInt("pageCount"), itemRs.getString("publishDate"),
                        itemRs.getInt("publishYear"), itemRs.getString("addedOn"),
                        itemRs.getString("language"), itemRs.getString("edition"),
                        itemRs.getString("volume"), itemRs.getFloat("rating"),
                        itemRs.getString("url"), itemRs.getString("review"),
                        itemRs.getString("cover"), itemRs.getString("filename"),
                        itemRs.getString("fileLocation"), itemRs.getString("narrator"),
                        itemRs.getString("bitrate"), itemRs.getString("boughtFrom"),
                        itemRs.getInt("sizeBytes"), itemRs.getInt("duration"),
                        itemRs.getInt("orderInSeries"));

                int relId = itemRs.getInt("publisherId");
                if (relId > 0) {
                    PreparedStatement pubStatement = connection.prepareStatement(relatedPublisherQry);
                    pubStatement.setInt(1, item.getId());
                    ResultSet pubRs = pubStatement.executeQuery();
                    if (pubRs.next()) {
                        item.setPublisher(new Publisher(pubRs.getInt("id"), pubRs.getString("name"),
                                pubRs.getString("summary"), pubRs.getString("website")));
                    }
                }

                relId = itemRs.getInt("seriesId");
                if (relId > 0) {
                    PreparedStatement seriesStatement = connection.prepareStatement(relatedSeriesQry);
                    seriesStatement.setInt(1, item.getId());
                    ResultSet seriesRs = seriesStatement.executeQuery();
                    if (seriesRs.next()) {
                        item.setSeries(new Series(seriesRs.getInt("id"), seriesRs.getString("name"),
                                seriesRs.getInt("completed") == 1, seriesRs.getInt("bookCount"),
                                seriesRs.getInt("ownedCount")));
                    }
                }

                relId = itemRs.getInt("collectionId");
                if (relId > 0) {
                    PreparedStatement colStatement = connection.prepareStatement(relatedCollectionQry);
                    colStatement.setInt(1, item.getId());
                    ResultSet colRs = colStatement.executeQuery();
                    if (colRs.next()) {
                        item.setCollection(new Collection(colRs.getInt("id"), colRs.getString("name"),
                                colRs.getString("publishDate"), colRs.getInt("publishYear"),
                                colRs.getString("description")));
                    }
                }

                //relId = rs.getInt("duplicatesId");
                //if(relId > 0) {
                //PreparedStatement relStatement = connection.prepareStatement();
                //ResultSet relRs = statement.executeQuery();
                //if (relRs.next()) {
                //TODO ...
                //}

                List<Author> authors = new ArrayList<>();
                PreparedStatement authorsStatement = connection.prepareStatement(relatedAuthorsQry);
                authorsStatement.setInt(1, item.getId());
                ResultSet authorsRs = authorsStatement.executeQuery();
                while (authorsRs.next()) {
                    authors.add(new Author(authorsRs.getInt("id"), authorsRs.getString("name"),
                            authorsRs.getString("surname"), authorsRs.getString("biography"),
                            authorsRs.getString("website")));
                }
                item.setAuthors(authors);

                List<Genre> genres = new ArrayList<>();
                PreparedStatement genresStatement = connection.prepareStatement(relatedGenresQry);
                genresStatement.setInt(1, item.getId());
                ResultSet genresRs = genresStatement.executeQuery();
                while (genresRs.next()) {
                    genres.add(new Genre(genresRs.getInt("id"), genresRs.getString("name")));
                }

                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableList(items);
    }
}
