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

import eu.sergiolopes.codices.models.Series;
import eu.sergiolopes.codices.repositories.SeriesRepository;
import eu.sergiolopes.codices.view.ViewManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SeriesController extends Controller {

    private static final int MAX_BOOK_COUNT = 2000;

    private SeriesRepository seriesRepository;
    private ObservableList<Series> bookSeries;
    private boolean isSearching;

    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;
    @FXML
    private ListView<Series> series;
    @FXML
    private TextField name;
    @FXML
    private Spinner<Integer> bookCount;
    @FXML
    private CheckBox completed;
    @FXML
    private TextField searchField;

    public SeriesController(ViewManager vm) {
        this(vm, "series.fxml");
    }

    public SeriesController(ViewManager vm, String fxml) {
        super(vm, fxml);
        isSearching = false;
        seriesRepository = new SeriesRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Manage Series";
    }

    @FXML
    private void initialize() {
        bookSeries = seriesRepository.findAll();
        bookCount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAX_BOOK_COUNT));

        series.setCellFactory(param -> {
            //TODO: handle update and pending changes
            return new ListCell<>() {
                @Override
                public void updateItem(Series series, boolean empty) {
                    super.updateItem(series, empty);
                    if (empty || series == null) {
                        setText(null);
                        setGraphic(null);
                        return;
                    }

                    setText(series.getName());
                }
            };
        });

        series.setItems(bookSeries);
        series.setOnMouseClicked(event -> {
            Series selected = series.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }

            setDetailsPanelData(selected.getName(), selected.getBookCount(), selected.isCompleted());
        });

        if (!series.getItems().isEmpty()) {
            Series first = series.getItems().get(0);

            setDetailsPanelData(first.getName(), first.getBookCount(), first.isCompleted());
            series.getSelectionModel().selectFirst();
        }
    }

    public void addSeries() {
        if (isSearching) {
            searchField.setText("");
            isSearching = false;
            series.setItems(bookSeries);
        }

        emptyDetailsPanelData();
        Series newSeries = new Series("<new series>");

        bookSeries.add(newSeries);
        setDetailsPanelData(newSeries.getName(), newSeries.getBookCount(), newSeries.isCompleted());
        series.getSelectionModel().selectLast();
    }

    public void deleteSelected() {
        MultipleSelectionModel<Series> selectionModel = series.getSelectionModel();
        Series selected = selectionModel.getSelectedItem();
        if (selected == null) {
            return;
        }

        int idx = selectionModel.getSelectedIndex();
        selectionModel.clearSelection();

        bookSeries.remove(selected);
        series.getItems().remove(idx);
        seriesRepository.delete(selected);

        emptyDetailsPanelData();
        if (idx - 1 >= 0) {
            selectionModel.select(idx - 1);
            setDetailsPanelData(selected.getName(), selected.getBookCount(), selected.isCompleted());
        }
    }

    public void saveChanges() {
        //TODO: Add validation
        Series updatedSeries = series.getSelectionModel().getSelectedItem();
        if (updatedSeries == null) {
            return;
        }

        String newText = name.getText();
        if (newText != null) {
            newText = newText.trim();
        }

        updatedSeries.setName(newText);
        updatedSeries.setBookCount(bookCount.getValueFactory().getValue());
        updatedSeries.setCompleted(completed.isSelected());

        seriesRepository.save(updatedSeries);
        series.refresh();
    }

    public void closeWindow() {
        getManager().closeCurrentStage();
    }

    public void search(KeyEvent event) {
        if (!isSearching) {
            series.getSelectionModel().clearSelection();
            isSearching = true;
        }

        String searchString = searchField.getText().trim();
        if (searchString.isBlank()) {
            series.setItems(bookSeries);
            isSearching = false;
            return;
        }

        ObservableList<Series> filtered = FXCollections.observableList(new ArrayList<>());
        for (var current : bookSeries) {
            if (current.getName().contains(searchString)) {
                filtered.add(current);
            }
        }

        series.setItems(filtered);
    }

    private void setDetailsPanelData(String name, int bookCount, boolean completed) {
        this.name.setText(name);
        this.bookCount.getValueFactory().setValue(bookCount);
        this.completed.setSelected(completed);
    }

    private void emptyDetailsPanelData() {
        setDetailsPanelData("", 0, false);
    }
}