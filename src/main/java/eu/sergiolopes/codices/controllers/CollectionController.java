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
import eu.sergiolopes.codices.repositories.CollectionRepository;
import eu.sergiolopes.codices.view.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CollectionController extends Controller implements Initializable {

    private CollectionRepository collectionRepository;

    @FXML
    private ListView<Collection> collections;

    @FXML
    private TextField name;
    @FXML
    private Spinner<Integer> publishYear;
    @FXML
    private DatePicker publishDate;

    public CollectionController(ViewManager vm, String fxml) {
        super(vm, fxml);
        collectionRepository = new CollectionRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Manage Collections";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        collections.setItems(collectionRepository.findAll());
        collections.setOnMouseClicked(event -> {
            var selected = collections.getSelectionModel().getSelectedItem();

            name.setText(selected.getName());
            publishDate.setValue(LocalDate.parse(selected.getPublishDate()));
            publishYear.getValueFactory().setValue(selected.getPublishYear());
        });

        if (!collections.getItems().isEmpty()) {
            Collection first = collections.getItems().get(0);

            name.setText(first.getName());
            publishDate.setValue(LocalDate.parse(first.getPublishDate()));
            publishYear.getValueFactory().setValue(first.getPublishYear());

            collections.getSelectionModel().selectFirst();
        }
    }

    public void addCollection() {
//        //TODO: set proper owner account
//        collections.getItems().add(new Collection("<new collection>", 1));
        name.setText("");
        publishDate.setValue(null);
        publishYear.getValueFactory().setValue(0);
        collections.getSelectionModel().selectLast();
    }

    public void deleteSelected() {
//        //TODO: Delete from list
        collectionRepository.delete(collections.getSelectionModel().getSelectedItem());
    }

    public void saveChanges() {
        name.setText("");
//        website.setText("");
        publishYear.getValueFactory().setValue(0);
        collectionRepository.save(null);
    }

    public void closeWindow() {
        getManager().closeCurrentStage();
    }

    public void changePhoto() {
        //TODO: upload photo and mark dirty
    }
}