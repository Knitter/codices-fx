package eu.sergiolopes.codices.models;

public class Author {

    private int id;
    private String name;
    private int ownedById;  //FOREIGN KEY (ownedById) REFERENCES account (id)
    private String surname;
    private String biography;
    private String website;
    private String photo;

    public Author(String name, int ownedById) {
        this(name, "", ownedById);
    }

    public Author(String name, String surname, int ownedById) {
        this(0, name, ownedById, surname, null, null, null);
    }

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
}
