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

public class Series {

    /**
     * Record's primary key. When 0 (zero) the record has not been saved to storage and exists only in memory.
     */
    private int id;

    /**
     * Series description/name.
     */
    private String name;

    /**
     * Flag marking the series as completed by the author.
     */
    private boolean completed;

    /**
     * Number of books that make up the series.
     */
    private int bookCount;

    /**
     * Number of books owned; should be automatically updated based on books added to the system and assigned this series.
     */
    private int ownedCount;

    public Series(String name) {
        this(0, name, false, 0, 0);
    }

    public Series(int id, String name, boolean completed, int bookCount, int ownedCount) {
        this.id = id;
        this.name = name;
        this.completed = completed;
        this.bookCount = bookCount;
        this.ownedCount = ownedCount;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public int getOwnedCount() {
        return ownedCount;
    }

    public void setOwnedCount(int ownedCount) {
        this.ownedCount = ownedCount;
    }
}
