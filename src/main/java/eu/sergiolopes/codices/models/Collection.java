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
package eu.sergiolopes.codices.models;

/**
 * A collection of books can be defined by a user, as a way to organize the list of books, or officially released by a
 * publisher, e.g. as part of a campaign or promotional event, etc.
 */
public class Collection {

    /**
     * Record's primary key. When 0 (zero) the record has not been saved to storage and exists only in memory.
     */
    private int id;

    /**
     * Collection name.
     */
    private String name;

    /**
     * Data for when the collection was published, if any.
     */
    private String publishDate;

    /**
     * Year for when the colleciton was published, usually when there is no specific date.
     */
    private int publishYear;

    /**
     * Short text describing the collection.
     */
    private String description;

    public Collection(String name) {
        this(0, name, null, 0, null);
    }

    public Collection(int id, String name, String publishDate, int publishYear, String description) {
        this.id = id;
        this.name = name;
        this.publishDate = publishDate;
        this.publishYear = publishYear;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}