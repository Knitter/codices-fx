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

import eu.sergiolopes.codices.models.Account;
import eu.sergiolopes.codices.repositories.AccountRepository;
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

public class AccountController extends Controller implements Initializable {

    private AccountRepository accountRepository;

    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;
    @FXML
    private ListView<Account> accounts;

//    @FXML
//    private ImageView photo;
//    @FXML
//    private TextField firstName;
//    @FXML
//    private TextField surname;
//    @FXML
//    private TextField website;
//    @FXML
//    private TextArea biography;

    public AccountController(ViewManager vm, String fxml) {
        super(vm, fxml);
        accountRepository = new AccountRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Manage Accounts";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accounts.setCellFactory(param -> {
            //TODO: handle update and pending changes
            return new ListCell<>() {
                @Override
                public void updateItem(Account account, boolean empty) {
                    super.updateItem(account, empty);
                    if (empty || account == null) {
                        setText(null);
                        setGraphic(null);
                        return;
                    }

                    setText(account.getUsername());
                }
            };
        });

        accounts.setItems(accountRepository.findAll());
        accounts.setOnMouseClicked(event -> {
            var selected = accounts.getSelectionModel().getSelectedItem();

//            firstName.setText(selected.getName());
//            surname.setText(selected.getSurname());
//            website.setText(selected.getWebsite());
//            biography.setText(selected.getBiography());
        });

        if (!accounts.getItems().isEmpty()) {
//            var first = authors.getItems().get(0);
//            firstName.setText(first.getName());
//            surname.setText(first.getSurname());
//            website.setText(first.getWebsite());
//            biography.setText(first.getBiography());
//
//            authors.getSelectionModel().selectFirst();
        }
    }

    public void addAuthor() {
        //TODO: set proper owner account
//        accounts.getItems().add(new Author("<new author>", 1));
//        firstName.setText("");
//        surname.setText("");
//        website.setText("");
//        biography.setText("");
//
//        authors.getSelectionModel().selectLast();
    }

    public void deleteSelected() {
        //TODO: Delete from list
//        authorRepository.delete(authors.getSelectionModel().getSelectedItem());
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

    public void changePhoto() {
        //TODO: upload photo and mark dirty
    }
}