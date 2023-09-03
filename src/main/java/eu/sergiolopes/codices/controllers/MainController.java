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

import eu.sergiolopes.codices.models.*;
import eu.sergiolopes.codices.repositories.ItemRepository;
import eu.sergiolopes.codices.view.ViewManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.control.SegmentedBar;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.tableview2.TableColumn2;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends Controller {

    private ItemType showing = ItemType.PAPER_BOOK;

    private FilteredTableView<Item> items;
    private ItemRepository itemRepository;
    private ObservableList<Item> paperBooks;
    private ObservableList<Item> audioBooks;
    private ObservableList<Item> eBooks;
    private boolean isSearching;
    private ObservableList<Item> selectedItems;

    private BookDetailsController bookDetailsPane;
    private EbookDetailsController ebookDetailsPane;
    private AudiobookDetailsController audiobookDetailsPane;

    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;
    @FXML
    private Label itemTitle;
    @FXML
    private Label itemAuthor;
    @FXML
    private Label itemPublisher;
    @FXML
    private Label itemDate;
    @FXML
    private Label itemDescription;
    @FXML
    private BorderPane contentContainer;
    @FXML
    private MasterDetailPane mainMasterDetailView;
    @FXML
    private Button addItem;
    @FXML
    private TextField searchField;
    @FXML
    private SegmentedBar<SegmentedBar.Segment> bookSegments;

    public MainController(ViewManager vm) {
        this(vm, "main-view.fxml");
    }

    public MainController(ViewManager vm, String fxml) {
        super(vm, fxml);
        isSearching = false;
        itemRepository = new ItemRepository(vm.getConnection());

        bookDetailsPane = new BookDetailsController();
        ebookDetailsPane = new EbookDetailsController();
        audiobookDetailsPane = new AudiobookDetailsController();
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
        getManager().showSettingsWindow();
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

    @FXML
    private void initialize() {
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

        //TODO: segment bottom bar
        //bookSegments.setSegmentViewFactory(param -> {
        //});
        //bookSegments.setInfoNodeFactory(segment -> new Label(segment.getText() + " " + segment.getValue() + " GB"));
        //bookSegments.getSegments().addAll(
        //        new SegmentedBar.Segment(0, "Audiobooks"),
        //        new SegmentedBar.Segment(0, "eBooks"),
        //        new SegmentedBar.Segment(0, "Books")
        //);
    }

    public void addItem() {
        //TODO: Event/callback
        switch (showing) {
            case EBOOK -> getManager().showEbookWindow();
            case AUDIO_BOOK -> getManager().showAudioBookWindow();
            case PAPER_BOOK -> getManager().showPaperBookWindow();
        }
    }

    public void search() {
        //TODO: ...
    }

    /**
     * Shows a details pane for the selected item, or hides the details pane if no item is selected.
     *
     * @param selected
     */
    private void displaySelectedItem(Item selected) {
        if (selected == null) {
            mainMasterDetailView.setShowDetailNode(false);
            return;
        }

        if (showing == ItemType.EBOOK) {
            ebookDetailsPane.setDetailsData(selected);
            mainMasterDetailView.setDetailNode(ebookDetailsPane);
        } else if (showing == ItemType.PAPER_BOOK) {
            bookDetailsPane.setDetailsData(selected);
            mainMasterDetailView.setDetailNode(bookDetailsPane);
        } else {
            audiobookDetailsPane.setDetailsData(selected);
            mainMasterDetailView.setDetailNode(audiobookDetailsPane);
        }

        mainMasterDetailView.setShowDetailNode(true);
    }

    public void showImportDialog() {
        getManager().showImportDialog();
    }

    public void showExportDialog() {
        //TODO: ...
    }
}