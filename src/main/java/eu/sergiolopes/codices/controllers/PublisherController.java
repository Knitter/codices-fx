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