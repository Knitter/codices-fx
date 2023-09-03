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

import eu.sergiolopes.codices.models.Publisher;
import eu.sergiolopes.codices.repositories.PublisherRepository;
import eu.sergiolopes.codices.view.ViewManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PublisherController extends Controller {

    private PublisherRepository publisherRepository;
    private ObservableList<Publisher> bookPublishers;
    private boolean isSearching;

    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;
    @FXML
    private ListView<Publisher> publishers;
    @FXML
    private TextField name;
    @FXML
    private TextField website;
    @FXML
    private TextArea summary;
    @FXML
    private TextField searchField;

    public PublisherController(ViewManager vm, String fxml) {
        super(vm, fxml);
        isSearching = false;
        publisherRepository = new PublisherRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Manage Publishers";
    }

    @FXML
    private void initialize() {
        bookPublishers = publisherRepository.findAll();
        publishers.setCellFactory(param -> {
            //TODO: handle update and pending changes
            return new ListCell<>() {
                @Override
                public void updateItem(Publisher publisher, boolean empty) {
                    super.updateItem(publisher, empty);
                    if (empty || publisher == null) {
                        setText(null);
                        setGraphic(null);
                        return;
                    }

                    setText(publisher.getName());
                }
            };
        });

        publishers.setItems(bookPublishers);
        publishers.setOnMouseClicked(event -> {
            Publisher selected = publishers.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }

            setDetailsPanelData(selected.getName(), selected.getWebsite(), selected.getSummary());
        });

        if (!publishers.getItems().isEmpty()) {
            Publisher first = publishers.getItems().get(0);

            setDetailsPanelData(first.getName(), first.getWebsite(), first.getSummary());
            publishers.getSelectionModel().selectFirst();
        }
    }

    public void addPublisher() {
        if (isSearching) {
            searchField.setText("");
            isSearching = false;
            publishers.setItems(bookPublishers);
        }

        emptyDetailsPanelData();
        Publisher newPublisher = new Publisher("<new publisher>");

        bookPublishers.add(newPublisher);
        setDetailsPanelData(newPublisher.getName(), newPublisher.getWebsite(), newPublisher.getSummary());
        publishers.getSelectionModel().selectLast();
    }

    public void deleteSelected() {
        MultipleSelectionModel<Publisher> selectionModel = publishers.getSelectionModel();
        Publisher selected = selectionModel.getSelectedItem();
        if (selected == null) {
            return;
        }

        int idx = selectionModel.getSelectedIndex();
        selectionModel.clearSelection();

        bookPublishers.remove(selected);
        publishers.getItems().remove(idx);
        publisherRepository.delete(selected);

        emptyDetailsPanelData();
        if (idx - 1 >= 0) {
            selectionModel.select(idx - 1);
            setDetailsPanelData(selected.getName(), selected.getWebsite(), selected.getSummary());
        }
    }

    public void saveChanges() {
        //TODO: Add validation
        Publisher updatedPublisher = publishers.getSelectionModel().getSelectedItem();
        if (updatedPublisher == null) {
            return;
        }

        String newText = name.getText();
        if (newText != null) {
            newText = newText.trim();
        }
        updatedPublisher.setName(newText);

        newText = website.getText();
        if (newText != null) {
            newText = newText.trim();
        }
        updatedPublisher.setWebsite(newText);

        newText = summary.getText();
        if (newText != null) {
            newText = newText.trim();
        }
        updatedPublisher.setSummary(newText);

        publisherRepository.save(updatedPublisher);
        publishers.refresh();
    }

    public void closeWindow() {
        getManager().closeCurrentStage();
    }

    public void search() {
        if (!isSearching) {
            publishers.getSelectionModel().clearSelection();
            isSearching = true;
        }

        String searchString = searchField.getText().trim();
        if (searchString.isBlank()) {
            publishers.setItems(bookPublishers);
            isSearching = false;
            return;
        }

        ObservableList<Publisher> filtered = FXCollections.observableList(new ArrayList<>());
        for (var current : bookPublishers) {
            if (current.getName().contains(searchString)) {
                filtered.add(current);
            }
        }

        publishers.setItems(filtered);
    }

    private void setDetailsPanelData(String name, String website, String summary) {
        this.name.setText(name);
        this.website.setText(website);
        this.summary.setText(summary);
    }

    private void emptyDetailsPanelData() {
        setDetailsPanelData("", "", "");
    }
}