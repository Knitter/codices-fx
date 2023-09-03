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
package eu.sergiolopes.codices.controllers;

import eu.sergiolopes.codices.models.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookDetailsController extends BorderPane {

    private Item book;
    @FXML
    private Label title;
    @FXML
    private Label author;
    @FXML
    private Label publisher;
    @FXML
    private Label date;
    @FXML
    private Label description;

    public BookDetailsController() {
        URL path = getClass().getResource("book-details.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(path);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        //scene.getStylesheets().clear();
        //scene.getStylesheets().add(getClass().getResource("/eu/sergiolopes/codices/css/default.css").toExternalForm());
        //scene.getStylesheets().add(getClass().getResource(ColorTheme.getCssPath(colorTheme)).toExternalForm());
        //scene.getStylesheets().add(getClass().getResource(FontSize.getCssPath(fontSize)).toExternalForm());
    }

    private void clearDetailsData() {
        //TODO: ... check if needed, delete if not
    }

    public void setDetailsData(Item book) {
        this.book = book;

        title.setText(book.getTitle());
        List<Author> authors = book.getAuthors();
        String authorsString = "?";
        if (authors != null && !authors.isEmpty()) {
            List names = new ArrayList(authors.size());
            for (Author author : authors) {
                names.add(author.getFullName());
            }

            authorsString = String.join("; ", names);
        }
        author.setText(authorsString);

        publisher.setText(book.getPublisher() != null ? book.getPublisher().getName() : "(?)");
        String publishDate = book.getPublishDate();
        if (publishDate == null || publishDate.isBlank()) {
            publishDate = book.getPublishYear() > 0 ? "" + book.getPublishYear() : "(?)";
        }
        date.setText(publishDate);

        String descriptionString = book.getPlot() != null ? book.getPlot() : "";
        if (descriptionString.isBlank()) {
            descriptionString = "No description provided.";
        }

        description.setText(descriptionString);
    }
}