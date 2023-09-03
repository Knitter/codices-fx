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

import java.util.List;

public class Item {

    /**
     * Record's primary key. When 0 (zero) the record has not been saved to storage and exists only in memory.
     */
    private int id;

    /**
     *
     */
    private String title;

    /**
     * The type of item: paper book (paper), eBook file (ebook), Audio Book file (audio)
     */
    private ItemType type;

    /**
     *
     */
    private boolean translated;

    /**
     * Flag, marks this book as having been read.
     */
    private boolean read;

    /**
     *
     */
    private int copies;

    /**
     *
     */
    private String subtitle;

    /**
     *
     */
    private String originalTitle;

    /**
     *
     */
    private String plot;

    /**
     *
     */
    private String isbn;

    /**
     *
     */
    private String format;

    /*

     */
    private int pageCount;

    /**
     *
     */
    private String publishDate;

    /**
     *
     */
    private int publishYear;

    /**
     *
     */
    private String addedOn;

    /**
     *
     */
    private String language;

    /**
     *
     */
    private String edition;

    /**
     *
     */
    private String volume;

    /**
     *
     */
    private float rating;

    /**
     *
     */
    private String url;

    /**
     *
     */
    private String review;

    /**
     *
     */
    //private String cover;

    /**
     *
     */
    private String filename;

    /**
     *
     */
    private String fileLocation;

    /**
     *
     */
    private String narrator;

    private String illustrator;
    private String dimensions;
    private String physicalLocation;
    private String loanedTo;
    private String loanedDate;
    private String borrowedFrom;
    private String borrowedDate;

    /**
     *
     */
    private String bitrate;

    /**
     *
     */
    private String purchasedFrom;

    /**
     *
     */
    private String purchasedOn;

    /**
     *
     */
    private int sizeBytes;

    /**
     * Duration of an audiobook, in minutes.
     */
    private int duration;

    /**
     *
     */
    private int orderInSeries;

    private List<Author> authors;
    private Publisher publisher;
    private Series series;
    private Collection collection;
    private Item duplicate;
    private List<Genre> genres;

    public Item(String title, ItemType type) {
        this(0, title, type, false, false, 1, null, null, null, null,
                null, 0, null, 0, null, null, null,
                null, 0, null, null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, null, 0, 0, 1);
    }

    public Item(int id, String title, ItemType type, boolean translated, boolean read, int copies, String subtitle,
                String originalTitle, String plot, String isbn, String format, int pageCount, String publishDate,
                int publishYear, String addedOn, String language, String edition, String volume, float rating, String url,
                String review, String cover, String filename, String fileLocation, String narrator, String illustrator,
                String dimensions, String physicalLocation, String loanedTo, String loanedDate, String borrowedFrom,
                String borrowedDate, String bitrate, String purchasedFrom, String purchasedOn, int sizeBytes,
                int duration, int orderInSeries) {

        this.id = id;
        this.title = title;
        this.type = type;
        this.translated = translated;
        this.read = read;
        this.copies = copies;
        this.subtitle = subtitle;
        this.originalTitle = originalTitle;
        this.plot = plot;
        this.isbn = isbn;
        this.format = format;
        this.pageCount = pageCount;
        this.publishDate = publishDate;
        this.publishYear = publishYear;
        this.addedOn = addedOn;
        this.language = language;
        this.edition = edition;
        this.volume = volume;
        this.rating = rating;
        this.url = url;
        this.review = review;
        //this.cover = cover;
        this.filename = filename;
        this.fileLocation = fileLocation;
        this.narrator = narrator;
        this.illustrator = illustrator;
        this.dimensions = dimensions;
        this.physicalLocation = physicalLocation;
        this.loanedTo = loanedTo;
        this.loanedDate = loanedDate;
        this.borrowedFrom = borrowedFrom;
        this.borrowedDate = borrowedDate;
        this.bitrate = bitrate;
        this.purchasedFrom = purchasedFrom;
        this.purchasedOn = purchasedOn;
        this.sizeBytes = sizeBytes;
        this.duration = duration;
        this.orderInSeries = orderInSeries;
    }

    public String getReadLabel() {
        return read ? "Yes" : "No";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public boolean isTranslated() {
        return translated;
    }

    public void setTranslated(boolean translated) {
        this.translated = translated;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    //public String getCover() {
    //    return cover;
    //}

    //public void setCover(String cover) {
    //    this.cover = cover;
    //}

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getNarrator() {
        return narrator;
    }

    public void setNarrator(String narrator) {
        this.narrator = narrator;
    }

    public String getIllustrator() {
        return illustrator;
    }

    public void setIllustrator(String illustrator) {
        this.illustrator = illustrator;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getPhysicalLocation() {
        return physicalLocation;
    }

    public void setPhysicalLocation(String physicalLocation) {
        this.physicalLocation = physicalLocation;
    }

    public String getLoanedTo() {
        return loanedTo;
    }

    public void setLoanedTo(String loanedTo) {
        this.loanedTo = loanedTo;
    }

    public String getLoanedDate() {
        return loanedDate;
    }

    public void setLoanedDate(String loanedDate) {
        this.loanedDate = loanedDate;
    }

    public String getBorrowedFrom() {
        return borrowedFrom;
    }

    public void setBorrowedFrom(String borrowedFrom) {
        this.borrowedFrom = borrowedFrom;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getPurchasedFrom() {
        return purchasedFrom;
    }

    public void setPurchasedFrom(String purchasedFrom) {
        this.purchasedFrom = purchasedFrom;
    }

    public String getPurchasedOn() {
        return purchasedOn;
    }

    public void setPurchasedOn(String purchasedOn) {
        this.purchasedOn = purchasedOn;
    }

    public int getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(int sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getOrderInSeries() {
        return orderInSeries;
    }

    public void setOrderInSeries(int orderInSeries) {
        this.orderInSeries = orderInSeries;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Item getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(Item duplicate) {
        this.duplicate = duplicate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
