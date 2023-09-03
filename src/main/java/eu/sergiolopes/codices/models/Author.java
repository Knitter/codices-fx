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

public class Author {

    /**
     * Record's primary key. When 0 (zero) the record has not been saved to storage and exists only in memory.
     */
    private int id;

    /**
     * Author's first name.
     */
    private String name;

    /**
     * Author's last name, usually show before the first name.
     */
    private String surname;

    /**
     * Author's biography.
     */
    private String biography;

    /**
     * URL to the author's website.
     */
    private String website;

    //TODO: needs review; was used to store file path in server version, won't work in desktop version
    //private String photo;

    /**
     * Creates a new Author using default values for most fields.
     *
     * @param name Author's first name.
     */
    public Author(String name) {
        this(0, name, null, null, null);
    }

    /**
     * Creates a new Author, allowing for all properties to be set when instantiating the object.
     *
     * @param id        Record's ID, used as primary key by storage (sqlite, mysql, etc.).
     * @param name      Author's first name.
     * @param surname   Author's last name, usually shown to users.
     * @param biography Biography text.
     * @param website   URL to the author's website
     */
    public Author(int id, String name, String surname, String biography, String website) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.biography = biography;
        this.website = website;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Builds a string with the author's full name, making sure the surname is shown first (if available).
     *
     * @return
     */
    public String getFullName() {
        if (name != null && !name.isBlank() && surname != null && !surname.isBlank()) {
            return surname + ", " + name;
        }

        if (name != null && !name.isBlank()) {
            return name;
        }

        return surname;
    }
}
