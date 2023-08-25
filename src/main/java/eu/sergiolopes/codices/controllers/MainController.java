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

import eu.sergiolopes.codices.models.Item;
import eu.sergiolopes.codices.repositories.ItemRepository;
import eu.sergiolopes.codices.view.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ImageInput;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.control.tableview2.FilteredTableView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends Controller implements Initializable {

    private FilteredTableView items;
    private ItemRepository itemRepository;

    @FXML
    private BorderPane contentContainer;

    public MainController(ViewManager vm, String fxml) {
        super(vm, fxml);
        itemRepository = new ItemRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Codices";
    }

    @FXML
    public void listAudioBooks() {
        System.out.printf("AudioBooks");
    }

    @FXML
    public void listEbooks() {
        System.out.println("Ebooks");
    }

    @FXML
    public void listPaperBooks() {
        System.out.println("Books");
    }

    @FXML
    public void showSettings() {
        System.out.println("Settings");
    }

    @FXML
    public void showAuthors() {
        getManager().showAuthorWindow();
    }

    @FXML
    public void showCollections() {

    }

    @FXML
    public void showPublishers() {

    }

    @FXML
    public void showSeries() {

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        items = new FilteredTableView();

        TableColumn<Item, Integer> firstNameColumn = new TableColumn<>("ID");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Item, String> lastNameColumn = new TableColumn<>("Title");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        items.getColumns().add(firstNameColumn);
        items.getColumns().add(lastNameColumn);

        items.getItems().addAll(itemRepository.findAll());
        contentContainer.setCenter(items);
    }
}