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
