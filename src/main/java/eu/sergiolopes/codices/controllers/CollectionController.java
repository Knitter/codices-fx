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

import eu.sergiolopes.codices.models.Collection;
import eu.sergiolopes.codices.models.Series;
import eu.sergiolopes.codices.repositories.CollectionRepository;
import eu.sergiolopes.codices.view.ViewManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CollectionController extends Controller implements Initializable {

    private CollectionRepository collectionRepository;

    @FXML
    private ListView<Collection> collections;
    private ObservableList<Collection> bookCollections;
    private boolean isSearching;

    @FXML
    private TextField name;
    @FXML
    private Spinner<Integer> publishYear;
    @FXML
    private DatePicker publishDate;
    @FXML
    private TextArea description;
    @FXML
    private TextField searchField;

    public CollectionController(ViewManager vm, String fxml) {
        super(vm, fxml);
        isSearching = false;
        collectionRepository = new CollectionRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Manage Collections";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookCollections = collectionRepository.findAll();
        publishYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, LocalDate.now().getYear()));

        collections.setCellFactory(param -> {
            //TODO: handle update and pending changes
            return new ListCell<>() {
                @Override
                public void updateItem(Collection collection, boolean empty) {
                    super.updateItem(collection, empty);
                    if (empty || collection == null) {
                        setText(null);
                        setGraphic(null);
                        return;
                    }

                    setText(collection.getName());
                }
            };
        });

        collections.setItems(bookCollections);
        collections.setOnMouseClicked(event -> {
            Collection selected = collections.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }

            setDetailsPanelData(selected.getName(), selected.getPublishDate(), selected.getPublishYear(), selected.getDescription());
        });

        if (!collections.getItems().isEmpty()) {
            Collection first = collections.getItems().get(0);

            setDetailsPanelData(first.getName(), first.getPublishDate(), first.getPublishYear(), first.getDescription());
            collections.getSelectionModel().selectFirst();
        }
    }

    public void addCollection() {
        if (isSearching) {
            searchField.setText("");
            isSearching = false;
            collections.setItems(bookCollections);
        }

        emptyDetailsPanelData();
        Collection newCollection = new Collection("<new collection>");

        bookCollections.add(newCollection);
        setDetailsPanelData(newCollection.getName(), newCollection.getPublishDate(), newCollection.getPublishYear(), newCollection.getDescription());
        collections.getSelectionModel().selectLast();
    }

    public void deleteSelected() {
        MultipleSelectionModel<Collection> selectionModel = collections.getSelectionModel();
        Collection selected = selectionModel.getSelectedItem();
        if (selected == null) {
            return;
        }

        int idx = selectionModel.getSelectedIndex();
        selectionModel.clearSelection();

        bookCollections.remove(selected);
        collections.getItems().remove(idx);
        collectionRepository.delete(selected);

        emptyDetailsPanelData();
        if (idx - 1 >= 0) {
            selectionModel.select(idx - 1);
            setDetailsPanelData(selected.getName(), selected.getPublishDate(), selected.getPublishYear(), selected.getDescription());
        }
    }

    public void saveChanges() {
        //TODO: Add validation
        Collection updatedCollection = collections.getSelectionModel().getSelectedItem();
        if (updatedCollection == null) {
            return;
        }

        String newText = name.getText();
        if (newText != null) {
            newText = newText.trim();
        }
        updatedCollection.setName(newText);

        newText = null;
        if (publishDate.getValue() != null) {
            newText = publishDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        updatedCollection.setPublishDate(newText);
        updatedCollection.setPublishYear(publishYear.getValue());

        collectionRepository.save(updatedCollection);
        collections.refresh();
    }

    public void closeWindow() {
        getManager().closeCurrentStage();
    }

    public void search() {
        if (!isSearching) {
            collections.getSelectionModel().clearSelection();
            isSearching = true;
        }

        String searchString = searchField.getText().trim();
        if (searchString.isBlank()) {
            collections.setItems(bookCollections);
            isSearching = false;
            return;
        }

        ObservableList<Collection> filtered = FXCollections.observableList(new ArrayList<>());
        for (var current : bookCollections) {
            if (current.getName().contains(searchString)) {
                filtered.add(current);
            }
        }

        collections.setItems(filtered);
    }

    private void setDetailsPanelData(String name, String date, int year, String description) {
        this.name.setText(name);
        this.publishDate.setValue(date != null && !date.isBlank() ? LocalDate.parse(date) : null);
        this.publishYear.getValueFactory().setValue(year);
        this.description.setText(description);
    }

    private void emptyDetailsPanelData() {
        setDetailsPanelData("", "", 0, "");
    }
}
