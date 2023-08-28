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

import eu.sergiolopes.codices.models.*;
import eu.sergiolopes.codices.repositories.ItemRepository;
import eu.sergiolopes.codices.view.ViewManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.tableview2.TableColumn2;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends Controller implements Initializable {

    private ItemType showing = ItemType.PAPER_BOOK;

    private FilteredTableView<Item> items;
    private ItemRepository itemRepository;
    private ObservableList<Item> paperBooks;
    private ObservableList<Item> audioBooks;
    private ObservableList<Item> eBooks;
    private boolean isSearching;
    private ObservableList<Item> selectedItems;

    @FXML
    private BorderPane contentContainer;
    @FXML
    private MasterDetailPane mainMasterDetailView;
    @FXML
    private TextField searchField;

    public MainController(ViewManager vm, String fxml) {
        super(vm, fxml);
        isSearching = false;
        itemRepository = new ItemRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Codices";
    }

    @FXML
    public void listAudioBooks() {
        if (showing != ItemType.AUDIO_BOOK) {
            showing = ItemType.AUDIO_BOOK;

            items.getItems().clear();
            items.getItems().addAll(itemRepository.findAllAudioBooks());
        }
    }

    @FXML
    public void listEbooks() {
        if (showing != ItemType.EBOOK) {
            showing = ItemType.EBOOK;
            items.getItems().clear();
            items.getItems().addAll(itemRepository.findAllEbooks());
        }
    }

    @FXML
    public void listPaperBooks() {
        if (showing != ItemType.PAPER_BOOK) {
            showing = ItemType.PAPER_BOOK;
            items.getItems().clear();
            items.getItems().addAll(itemRepository.findAllPaperBooks());
        }
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
        getManager().showCollectionsWindow();
    }

    @FXML
    public void showPublishers() {
        getManager().showPublishersWindow();
    }

    @FXML
    public void showSeries() {
        getManager().showSeriesWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        items = new FilteredTableView<>();
        items.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> displaySelectedItem(newValue));

        TableColumn2<Item, String> col = new TableColumn2<>("Title");
        col.setCellValueFactory(new PropertyValueFactory<>("title"));
        items.getColumns().add(col);

        col = new TableColumn2<>("Author");
        col.setCellValueFactory(param -> {
            List<Author> authors = param.getValue().getAuthors();

            String authorsString = "";
            if (authors != null && !authors.isEmpty()) {
                List names = new ArrayList(authors.size());
                for (Author author : authors) {
                    names.add(author.getFullName());
                }

                authorsString = String.join("; ", names);
            }

            return new SimpleObjectProperty<>(authorsString);
        });
        items.getColumns().add(col);

        col = new TableColumn2<>("Series");
        col.setCellValueFactory(param -> {
            String name = "";
            Series series = param.getValue().getSeries();
            if (series != null) {
                name = series.getName();
            }

            return new SimpleObjectProperty<>(name);
        });
        items.getColumns().add(col);

        col = new TableColumn2<>("Genre");
        col.setCellValueFactory(param -> {
            List<Genre> genres = param.getValue().getGenres();

            String genresString = "";
            if (genres != null && !genres.isEmpty()) {
                List names = new ArrayList(genres.size());
                for (Genre genre : genres) {
                    names.add(genre.getName());
                }

                genresString = String.join(", ", names);
            }

            return new SimpleObjectProperty<>(genresString);
        });
        items.getColumns().add(col);

        col = new TableColumn2<>("Format");
        col.setCellValueFactory(new PropertyValueFactory<>("format"));
        items.getColumns().add(col);

        col = new TableColumn2<>("Pages");
        col.setCellValueFactory(new PropertyValueFactory<>("pageCount"));
        items.getColumns().add(col);

        col = new TableColumn2<>("ISBN");
        col.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        items.getColumns().add(col);

        TableColumn2<Item, Boolean> readCol = new TableColumn2<>("Read?");
        readCol.setCellValueFactory(param -> new SimpleBooleanProperty(param.getValue().isRead()));
        readCol.setCellFactory(CheckBoxTableCell.forTableColumn(readCol));
        readCol.setPrefWidth(60);
        items.getColumns().add(readCol);

        paperBooks = itemRepository.findAllPaperBooks();
        eBooks = itemRepository.findAllEbooks();
        audioBooks = itemRepository.findAllAudioBooks();

        items.setItems(paperBooks);
        contentContainer.setCenter(items);
    }
}