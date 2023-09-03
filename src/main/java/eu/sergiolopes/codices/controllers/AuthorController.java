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
import eu.sergiolopes.codices.repositories.AuthorRepository;
import eu.sergiolopes.codices.view.ViewManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AuthorController extends Controller {

    private AuthorRepository authorRepository;
    private ObservableList<Author> bookAuthors;
    private boolean isSearching;

    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;
    @FXML
    private ListView<Author> authors;
    @FXML
    private ImageView photo;
    @FXML
    private TextField firstName;
    @FXML
    private TextField surname;
    @FXML
    private TextField website;
    @FXML
    private TextArea biography;
    @FXML
    private TextField searchField;

    public AuthorController(ViewManager vm) {
        this(vm, "authors.fxml");
    }

    public AuthorController(ViewManager vm, String fxml) {
        super(vm, fxml);
        isSearching = false;
        authorRepository = new AuthorRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Manage Authors";
    }

    @FXML
    private void initialize() {
        bookAuthors = authorRepository.findAll();

        authors.setCellFactory(param -> {
            //TODO: handle update and pending changes
            return new ListCell<>() {
                @Override
                public void updateItem(Author author, boolean empty) {
                    super.updateItem(author, empty);
                    if (empty || author == null) {
                        setText(null);
                        setGraphic(null);
                        return;
                    }

                    setText(author.getFullName());
                }
            };
        });

        authors.setItems(bookAuthors);
        authors.setOnMouseClicked(event -> {
            Author selected = authors.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }

            setDetailsPanelData(selected.getName(), selected.getSurname(), selected.getWebsite(), selected.getBiography());
        });

        if (!authors.getItems().isEmpty()) {
            var first = authors.getItems().get(0);

            setDetailsPanelData(first.getName(), first.getSurname(), first.getWebsite(), first.getBiography());
            authors.getSelectionModel().selectFirst();
        }
    }

    public void addAuthor() {
        //TODO: set proper owner account
        if (isSearching) {
            searchField.setText("");
            isSearching = false;
            authors.setItems(bookAuthors);
        }

        emptyDetailsPanelData();
        Author newAuthor = new Author("<new author>");

        bookAuthors.add(newAuthor);
        setDetailsPanelData(newAuthor.getName(), newAuthor.getSurname(), newAuthor.getWebsite(), newAuthor.getBiography());
        authors.getSelectionModel().selectLast();
    }

    public void deleteSelected() {
        MultipleSelectionModel<Author> selectionModel = authors.getSelectionModel();
        Author selected = selectionModel.getSelectedItem();
        if (selected == null) {
            return;
        }

        int idx = selectionModel.getSelectedIndex();
        selectionModel.clearSelection();

        bookAuthors.remove(selected);
        authors.getItems().remove(idx);
        authorRepository.delete(selected);

        emptyDetailsPanelData();
        if (idx - 1 >= 0) {
            selectionModel.select(idx - 1);
            setDetailsPanelData(selected.getName(), selected.getSurname(), selected.getWebsite(), selected.getBiography());
        }
    }

    public void saveChanges() {
        //TODO: Add validation
        Author updatedAuthor = authors.getSelectionModel().getSelectedItem();
        if (updatedAuthor == null) {
            return;
        }

        String newText = firstName.getText();
        if (newText != null) {
            newText = newText.trim();
        }
        updatedAuthor.setName(newText);

        newText = surname.getText();
        if (newText != null) {
            newText = newText.trim();
        }
        updatedAuthor.setSurname(newText);

        newText = website.getText();
        if (newText != null) {
            newText = newText.trim();
        }
        updatedAuthor.setWebsite(newText);

        newText = biography.getText();
        if (newText != null) {
            newText = newText.trim();
        }
        updatedAuthor.setBiography(newText);

        authorRepository.save(updatedAuthor);
        authors.refresh();
    }

    public void closeWindow() {
        getManager().closeCurrentStage();
    }

    public void changePhoto() {
        //TODO: Not implemented yet.
    }

    public void search() {
        if (!isSearching) {
            authors.getSelectionModel().clearSelection();
            isSearching = true;
        }

        String searchString = searchField.getText().trim();
        if (searchString.isBlank()) {
            authors.setItems(bookAuthors);
            isSearching = false;
            return;
        }

        ObservableList<Author> filtered = FXCollections.observableList(new ArrayList<>());
        for (var current : bookAuthors) {
            if (current.getName().contains(searchString) ||
                    (current.getSurname() != null && current.getSurname().contains(searchString))) {

                filtered.add(current);
            }
        }

        authors.setItems(filtered);
    }


    private void setDetailsPanelData(String name, String surname, String website, String biography) {
        this.firstName.setText(name);
        this.surname.setText(surname);
        this.website.setText(website);
        this.biography.setText(biography);
    }

    private void emptyDetailsPanelData() {
        setDetailsPanelData("", "", "", "");
    }
}