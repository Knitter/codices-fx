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
import eu.sergiolopes.codices.repositories.*;
import eu.sergiolopes.codices.view.ViewManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.SearchableComboBox;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BookController extends Controller {

    private static final int MAX_COPIES = 500;
    private static final int MAX_ORDER = 500;
    private static final int MAX_PAGE_COUNT = 50000;

    private AuthorRepository authorRepository;
    private CollectionRepository collectionRepository;
    private FormatRepository formatRepository;
    private ItemRepository bookRepository;
    private PublisherRepository publisherRepository;
    private SeriesRepository seriesRepository;
    private GenreRepository genreRepository;

    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;

    @FXML
    private TextField title;
    @FXML
    private TextField subtitle;
    @FXML
    private TextField volume;
    @FXML
    private Spinner<Integer> order;
    @FXML
    private DatePicker publishDate;
    @FXML
    private Spinner<Integer> publishYear;
    @FXML
    private TextArea plot;
    @FXML
    private TextArea review;
    @FXML
    private TextField originalTitle;
    @FXML
    private TextField edition;
    @FXML
    private Spinner<Integer> pages;
    @FXML
    private TextField dimensions;
    @FXML
    private CheckBox read;
    @FXML
    private SearchableComboBox<Series> series;
    @FXML
    private SearchableComboBox<Author> author;
    @FXML
    private SearchableComboBox<Publisher> publisher;
    @FXML
    private SearchableComboBox<Format> format;
    @FXML
    private SearchableComboBox<String> language;
    @FXML
    private CheckComboBox<Genre> genre;
    @FXML
    private ImageView cover;
    @FXML
    private TextField isbn;
    @FXML
    private TextField url;
    @FXML
    private TextField physicalLocation;
    @FXML
    private DatePicker borrowedDate;
    @FXML
    private DatePicker loanedDate;
    @FXML
    private SearchableComboBox<String> purchasedFrom;
    @FXML
    private SearchableComboBox<Item> duplicates;
    @FXML
    private SearchableComboBox<String> illustrator;
    @FXML
    private SearchableComboBox<Collection> collection;
    @FXML
    private SearchableComboBox<String> borrowedFrom;
    @FXML
    private SearchableComboBox<String> loanedTo;
    @FXML
    private DatePicker purchaseDate;
    @FXML
    private Spinner<Integer> copies;

    private Item book;

    public BookController(ViewManager vm) {
        this(vm, null);
    }

    public BookController(ViewManager vm, Item book) {
        this(vm, "add-book.fxml", book);
    }

    public BookController(ViewManager vm, String fxml, Item book) {
        super(vm, fxml);
        this.book = book;

        authorRepository = new AuthorRepository(vm.getConnection());
        collectionRepository = new CollectionRepository(vm.getConnection());
        formatRepository = new FormatRepository(vm.getConnection());
        bookRepository = new ItemRepository(vm.getConnection());
        publisherRepository = new PublisherRepository(vm.getConnection());
        seriesRepository = new SeriesRepository(vm.getConnection());
        genreRepository = new GenreRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Book Details";
    }

    @FXML
    private void initialize() {
        publishYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, LocalDate.now().getYear()));
        copies.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAX_COPIES));
        pages.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAX_PAGE_COUNT));
        order.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, MAX_ORDER));

        series.setItems(seriesRepository.findAll());
        author.setItems(authorRepository.findAll());
        publisher.setItems(publisherRepository.findAll());
        format.setItems(formatRepository.findAllForType(ItemType.PAPER_BOOK.getType()));

        cover.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                FileChooser fc = new FileChooser();
                //fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Supported images formats (BMP, GIF, JPG, PNG)", extensions));
                //fc.setInitialDirectory(Paths.get(System.getProperty("user.home")).toFile());
                File selected = fc.showOpenDialog(getStage().getOwner());

                if (selected != null) {
                    cover.setImage(new Image(selected.toString()));
                }

                event.consume();
            }
        });

        Connection connection = getManager().getConnection();
        String formatQry = "SELECT DISTINCT language FROM item WHERE ownedById = 1 ORDER BY language";
        String purchasedFromQry = "SELECT DISTINCT purchasedFrom FROM item WHERE ownedById = 1 ORDER BY purchasedFrom";
        String borrowedFromQry = "SELECT DISTINCT borrowedFrom FROM item WHERE ownedById = 1 ORDER BY borrowedFrom";
        String loanedToQry = "SELECT DISTINCT loanedTo FROM item WHERE ownedById = 1 ORDER BY loanedTo";
        String illustratorQry = "SELECT DISTINCT illustrator FROM item WHERE ownedById = 1 ORDER BY illustrator";

        List<String> languages = new ArrayList<>();
        List<String> purchaseFromList = new ArrayList<>();
        List<String> borrowedFromList = new ArrayList<>();
        List<String> loanedToList = new ArrayList<>();
        List<String> illustrators = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(formatQry);
            ResultSet rs = statement.executeQuery();
            String value;
            while (rs.next()) {
                value = rs.getString("language");
                if (value != null && !value.isEmpty()) {
                    languages.add(value);
                }
            }
            rs.close();

            if (!languages.isEmpty()) {
                language.setItems(FXCollections.observableList(languages));
            }

            statement = connection.prepareStatement(purchasedFromQry);
            rs = statement.executeQuery();
            while (rs.next()) {
                value = rs.getString("purchasedFrom");
                if (value != null && !value.isEmpty()) {
                    purchaseFromList.add(value);
                }
            }
            rs.close();

            if (!purchaseFromList.isEmpty()) {
                purchasedFrom.setItems(FXCollections.observableList(purchaseFromList));
            }

            statement = connection.prepareStatement(borrowedFromQry);
            rs = statement.executeQuery();
            while (rs.next()) {
                value = rs.getString("borrowedFrom");
                if (value != null && !value.isEmpty()) {
                    borrowedFromList.add(value);
                }
            }
            rs.close();

            if (!borrowedFromList.isEmpty()) {
                borrowedFrom.setItems(FXCollections.observableList(borrowedFromList));
            }

            statement = connection.prepareStatement(loanedToQry);
            rs = statement.executeQuery();
            while (rs.next()) {
                value = rs.getString("loanedTo");
                if (value != null && !value.isEmpty()) {
                    loanedToList.add(value);
                }
            }
            rs.close();

            if (!loanedToList.isEmpty()) {
                loanedTo.setItems(FXCollections.observableList(loanedToList));
            }

            statement = connection.prepareStatement(illustratorQry);
            rs = statement.executeQuery();
            while (rs.next()) {
                value = rs.getString("illustrator");
                if (value != null && !value.isEmpty()) {
                    illustrators.add(value);
                }
            }
            rs.close();

            if (!illustrators.isEmpty()) {
                illustrator.setItems(FXCollections.observableList(illustrators));
            }
        } catch (SQLException e) {
            //TODO: Proper handling
            e.printStackTrace();
        }

        genre.setConverter(new StringConverter<>() {
            @Override
            public String toString(Genre genre) {
                return genre.getName();
            }

            @Override
            public Genre fromString(String string) {
                return null;
            }
        });
        genre.getItems().addAll(genreRepository.findAll());
        collection.setItems(collectionRepository.findAll());
        //duplicates;
        //publishDate;
        //borrowedDate;
        //loanedDate;
        //purchaseDate;

        if (book != null) {
            title.setText(book.getTitle());
            subtitle.setText(book.getSubtitle());
            volume.setText(book.getVolume());
            order.getValueFactory().setValue(book.getOrderInSeries());
            //private DatePicker publishDate;
            publishYear.getValueFactory().setValue(book.getPublishYear());
            plot.setText(book.getPlot());
            review.setText(book.getReview());
            originalTitle.setText(book.getOriginalTitle());
            edition.setText(book.getEdition());
            pages.getValueFactory().setValue(book.getPageCount());
            dimensions.setText(book.getDimensions());
            read.setSelected(book.isRead());
            //ImageView cover;
            isbn.setText(book.getIsbn());
            url.setText(book.getUrl());
            physicalLocation.setText(book.getPhysicalLocation());
            //borrowedDate;
            //loanedDate;
            //purchasedFrom.getSelectionModel().set
            //duplicates;
            //illustrator;
            //collection;
            //borrowedFrom;
            //loanedTo;
            //purchaseDate;
            copies.getValueFactory().setValue(book.getCopies());
        }
    }

    public void saveChanges() {
        if (book == null) {
            book = new Item("", ItemType.PAPER_BOOK);
        }

        book.setTitle(title.getText());
        book.setSubtitle(subtitle.getText());
        book.setVolume(volume.getText());
        //order;
        //publishDate;
        //publishYear;
        book.setPlot(plot.getText());
        book.setReview(review.getText());

        String oTitle = originalTitle.getText();
        book.setOriginalTitle(oTitle);
        if (oTitle != null && !oTitle.isBlank()) {
            book.setTranslated(true);
        }

        book.setEdition(edition.getText());
        //pages;
        book.setDimensions(dimensions.getText());
        book.setRead(read.isSelected());
        //SearchableComboBox<Series> series;
        //SearchableComboBox<Author> author;
        //SearchableComboBox<Publisher> publisher;
        //SearchableComboBox<Format> format;
        //SearchableComboBox<String> language;
        //CheckComboBox<Genre> genre;
        //ImageView cover;
        book.setIsbn(isbn.getText());
        book.setUrl(url.getText());
        book.setPhysicalLocation(physicalLocation.getText());
        //borrowedDate;
        //loanedDate;
        //SearchableComboBox<String> purchasedFrom;
        //SearchableComboBox<Item> duplicates;
        //SearchableComboBox<String> illustrator;
        //SearchableComboBox<Collection> collection;
        //SearchableComboBox<String> borrowedFrom;
        //SearchableComboBox<String> loanedTo;
        //purchaseDate;
        //Spinner<Integer> copies;
        bookRepository.save(book);
        //TODO: update parent
        closeWindow();
    }
}