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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class PublisherController extends Controller implements Initializable {

    private PublisherRepository publisherRepository;

    @FXML
    private ListView<Publisher> publishers;

    @FXML
    private ImageView logo;
    @FXML
    private TextField name;
    @FXML
    private TextField website;
    @FXML
    private TextArea summary;

    public PublisherController(ViewManager vm, String fxml) {
        super(vm, fxml);
        publisherRepository = new PublisherRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Manage Publishers";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        Borders.wrap(photo)
//                .lineBorder()
//                .title("Photo")
//                .color(Color.GREEN)
//                .thickness(1, 0, 0, 0)
//                .thickness(1)
//                .radius(0, 5, 5, 0)
//                .build();

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
        publishers.setItems(publisherRepository.findAll());
        publishers.setOnMouseClicked(event -> {
            Publisher selected = publishers.getSelectionModel().getSelectedItem();

            name.setText(selected.getName());
            website.setText(selected.getWebsite());
            summary.setText(selected.getSummary());
        });

        if (!publishers.getItems().isEmpty()) {
            Publisher first = publishers.getItems().get(0);
            name.setText(first.getName());
            website.setText(first.getWebsite());
            summary.setText(first.getSummary());

            publishers.getSelectionModel().selectFirst();
        }
    }

    public void addPublisher() {
        //TODO: set proper owner account
//        publishers.getItems().add(new Author("<new author>", 1));
//        firstName.setText("");
//        surname.setText("");
//        website.setText("");
//        biography.setText("");
//
//        publishers.getSelectionModel().selectLast();
    }

    public void deleteSelected() {
        //TODO: Delete from list
        publisherRepository.delete(publishers.getSelectionModel().getSelectedItem());
    }

    public void saveChanges() {
//        firstName.setText("");
//        surname.setText("");
//        website.setText("");
//        biography.setText("");
//        authorRepository.save(null);
    }

    public void closeWindow() {
        getManager().closeCurrentStage();
    }

    public void search() {
        //TODO: ...
    }
}