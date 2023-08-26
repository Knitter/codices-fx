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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SeriesController extends Controller implements Initializable {

    private SeriesRepository seriesRepository;

    @FXML
    private ListView<Series> series;

    @FXML
    private TextField name;
    @FXML
    private Spinner<Integer> bookCount;
    @FXML
    private Spinner<Integer> ownedCount;
    @FXML
    private CheckBox completed;

    public SeriesController(ViewManager vm, String fxml) {
        super(vm, fxml);
        seriesRepository = new SeriesRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Manage Series";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookCount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2000));
        ownedCount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2000));

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
        series.setItems(seriesRepository.findAll());
        series.setOnMouseClicked(event -> {
            Series selected = series.getSelectionModel().getSelectedItem();

            name.setText(selected.getName());
            bookCount.getValueFactory().setValue(selected.getBookCount());
            ownedCount.getValueFactory().setValue(selected.getOwnedCount());
            completed.setSelected(selected.isCompleted());
        });

        if (!series.getItems().isEmpty()) {
            Series first = series.getItems().get(0);

            name.setText(first.getName());
            bookCount.getValueFactory().setValue(first.getBookCount());
            ownedCount.getValueFactory().setValue(first.getOwnedCount());
            completed.setSelected(first.isCompleted());

            series.getSelectionModel().selectFirst();
        }
    }

    public void addSeries() {
        //TODO: set proper owner account
        //series.getItems().add(new Series("<new series>", 1));
        name.setText("");
        bookCount.getValueFactory().setValue(0);
        ownedCount.getValueFactory().setValue(0);
        completed.setSelected(false);

        series.getSelectionModel().selectLast();
    }

    public void deleteSelected() {
        //TODO: Delete from list
        seriesRepository.delete(series.getSelectionModel().getSelectedItem());
    }

    public void saveChanges() {
//        name.setText("");
//        seriesRepository.save(null);
    }

    public void closeWindow() {
        getManager().closeCurrentStage();
    }
}