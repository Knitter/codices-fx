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

import eu.sergiolopes.codices.models.Series;
import eu.sergiolopes.codices.repositories.SeriesRepository;
import eu.sergiolopes.codices.view.ViewManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SeriesController extends Controller implements Initializable {

    private static final int MAX_BOOK_COUNT = 2000;

    private SeriesRepository seriesRepository;
    private ObservableList<Series> bookSeries;
    private boolean isSearching;

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

    public SeriesController(ViewManager vm, String fxml) {
        super(vm, fxml);
        isSearching = false;
        seriesRepository = new SeriesRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Manage Series";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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