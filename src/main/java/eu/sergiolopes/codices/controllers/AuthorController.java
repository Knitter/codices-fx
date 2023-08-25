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

import eu.sergiolopes.codices.models.Author;
import eu.sergiolopes.codices.repositories.AuthorRepository;
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

public class AuthorController extends Controller implements Initializable {

    private AuthorRepository authorRepository;

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

    public AuthorController(ViewManager vm, String fxml) {
        super(vm, fxml);
        authorRepository = new AuthorRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Manage Authors";
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

        authors.setCellFactory(param -> {
            //TODO: handle update and pending changes
            return new ListCell<>() {
                @Override
                public void updateItem(Author author, boolean empty) {
                    super.updateItem(author, empty);
                    if (empty || author == null) {
                        setText(null);
                    } else {
                        setText(author.getSurname() + ", " + author.getName());
                    }
                }
            };
        });
        authors.setItems(authorRepository.findAll());
        authors.setOnMouseClicked(event -> {
            var selected = authors.getSelectionModel().getSelectedItem();

            firstName.setText(selected.getName());
            surname.setText(selected.getSurname());
            website.setText(selected.getWebsite());
            biography.setText(selected.getBiography());
        });

        if (!authors.getItems().isEmpty()) {
            var first = authors.getItems().get(0);
            firstName.setText(first.getName());
            surname.setText(first.getSurname());
            website.setText(first.getWebsite());
            biography.setText(first.getBiography());

            authors.getSelectionModel().selectFirst();
        }
    }

    public void addAuthor() {
        //TODO: set proper owner account
        authors.getItems().add(new Author("<new author>", 1));
        firstName.setText("");
        surname.setText("");
        website.setText("");
        biography.setText("");

        authors.getSelectionModel().selectLast();
    }

    public void deleteSelected() {
        //TODO: Delete from list
        authorRepository.delete(authors.getSelectionModel().getSelectedItem());
    }

    public void saveChanges() {
//        firstName.setText("");
//        surname.setText("");
//        website.setText("");
//        biography.setText("");
        authorRepository.save(null);
    }

    public void closeWindow() {
        getManager().closeCurrentStage();
    }

    public void changePhoto() {
        //TODO: upload photo and mark dirty
    }
}