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
     *
     */
    private String name;

    /**
     *
     */
    private int ownedById;  //FOREIGN KEY (ownedById) REFERENCES account (id)

    /**
     *
     */
    private String surname;

    /**
     *
     */
    private String biography;

    /**
     *
     */
    private String website;

    /**
     * //TODO: needs review; was used to store file path in server version, won't work in desktop version
     */
    private String photo;

    /**
     * Creates a new Author using default values for most fields.
     *
     * @param name      Author's first name.
     * @param ownedById ID of the account that owns this record.
     */
    public Author(String name, int ownedById) {
        this(0, name, ownedById, null, null, null, null);
    }

    /**
     * Creates a new Author, allowing for all properties to be set when instantiating the object.
     *
     * @param id        Record's ID, used as primary key by storage (sqlite, mysql, etc.).
     * @param name      Author's first name.
     * @param ownedById ID of the account that owns this record.
     * @param surname   Author's last name, usually shown to users.
     * @param biography Biography text.
     * @param website   URL to the author's website
     * @param photo
     */
    public Author(int id, String name, int ownedById, String surname, String biography, String website, String photo) {
        this.id = id;
        this.name = name;
        this.ownedById = ownedById;
        this.surname = surname;
        this.biography = biography;
        this.website = website;
        this.photo = photo;
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

    public int getOwnedById() {
        return ownedById;
    }

    public void setOwnedById(int ownedById) {
        this.ownedById = ownedById;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

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
