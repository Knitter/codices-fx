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

import eu.sergiolopes.codices.models.Author;
import eu.sergiolopes.codices.models.Item;
import eu.sergiolopes.codices.models.Publisher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AudioBookController extends BorderPane {

    private Item book;
    @FXML
    private Label itemTitle;
    @FXML
    private Label itemAuthor;
    @FXML
    private Label itemPublisher;
    @FXML
    private Label itemDate;
    @FXML
    private Label itemDescription;

    public AudioBookController() {
        URL path = getClass().getResource("audiobook-details.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(path);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void clearDetailsData() {
        //TODO: ... check if needed, delete if not
    }

    public void setDetailsData(Item book) {
        this.book = book;

        itemTitle.setText(book.getTitle());
        List<Author> authors = book.getAuthors();
        String authorsString = "?";
        if (authors != null && !authors.isEmpty()) {
            List names = new ArrayList(authors.size());
            for (Author author : authors) {
                names.add(author.getFullName());
            }

            authorsString = String.join("; ", names);
        }
        itemAuthor.setText("by " + authorsString);

        Publisher publisher = book.getPublisher();
        if (publisher != null) {
            itemPublisher.setText("published by " + publisher.getName());
        }

        String description = book.getPlot() != null ? book.getPlot() : "";
        if (description.isBlank()) {
            description = "No description provided.";
        }

        itemDescription.setText(description);
    }
}