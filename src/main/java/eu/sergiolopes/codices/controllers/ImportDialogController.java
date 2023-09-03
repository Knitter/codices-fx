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

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import eu.sergiolopes.codices.view.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ImportDialogController extends Controller {

    private static final int BOOK_BUDDY_CSV = 0;
    private static final int CALIBRE = 1;
    private static final int CODICES_CSV = 2;
    private static final int CODICES_JSON = 3;

    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;
    @FXML
    private ChoiceBox<String> importFormat;
    @FXML
    private TextField importFile;

    public ImportDialogController(ViewManager vm) {
        super(vm, "import-dialog.fxml");
    }

    public ImportDialogController(ViewManager vm, String fxml) {
        super(vm, fxml);
    }

    @Override
    public String getTitle() {
        return "Import Items";
    }

    @FXML
    private void initialize() {
    }

    public void processFile() {
        String path = importFile.getText().trim();
        if (path.isBlank()) {
            //TODO ..
            return;
        }

        File file = new File(path);
        if (!file.isFile()) {
            //TODO: ...
            return;
        }

        switch (importFormat.getSelectionModel().getSelectedIndex()) {
            case BOOK_BUDDY_CSV:
                importFromBookBuddy(file);
                break;
            case CALIBRE:
                importFromCalibre(file);
                break;
            case CODICES_CSV:
                importFromCodicesCSV(file);
                break;
            case CODICES_JSON:
                importFromCodicesJSON(file);
                break;
        }

        //TODO: ...
    }

    public void browseFiles() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(Paths.get(System.getProperty("user.home")).toFile());

        File selected = fc.showOpenDialog(getStage().getOwner());
        if (selected != null) {
            importFile.setText(selected.toString());
        }
    }

    private void importFromBookBuddy(File file) {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();

        try {
            CSVReader csvReader = new CSVReaderBuilder(Files.newBufferedReader(file.toPath()))
                    .withSkipLines(0)
                    .withCSVParser(parser)
                    .build();



            // Title
            // Original Title
            // Subtitle
            // Series
            // Volume
            // Author
            // "Author (Last, First)"
            // Illustrator
            // Narrator
            // Translator
            // Photographer
            // Editor
            // Publisher
            // Place of Publication
            // Date Published
            // Year Published
            // Original Date Published
            // Original Year Published
            // Edition
            // Genre
            // Summary
            // Guided Reading Level
            // Lexile Measure
            // Lexile Code
            // Grade Level Equivalent
            // Developmental Reading Assessment
            // Interest Level
            // AR Level
            // AR Points
            // AR Quiz Number
            // Word Count
            // Number of Pages
            // Format
            // Audio Runtime
            // Dimensions
            // Weight
            // List Price
            // Language
            // Original Language
            // DDC
            // LCC
            // LCCN
            // OCLC
            // ISBN
            // ISSN
            // Favorites
            // Rating
            // Physical Location
            // Status
            // Status Incompleted Reason
            // Status Hidden
            // Date Started
            // Date Finished
            // Current Page
            // Loaned To
            // Date Loaned
            // Borrowed From
            // Date Borrowed
            // Returned from Borrow
            // Not Owned Reason
            // Quantity
            // Condition
            // Recommended By
            // Date Added
            // User Supplied ID
            // User Supplied Descriptor
            // Tags
            // Purchase Date
            // Purchase Place
            // Purchase Price
            // Notes
            // Google VolumeID
            // Category
            // Wish List
            // Previously Owned
            // Up Next
            // Position
            // Uploaded Image URL
            // Activities
            //1Q84,,,,,Haruki Murakami,"Murakami, Haruki",,,,,,Vintage / Random House,,,2012,,,,Fiction,"The year is 1Q84.This is the real world, there is no doubt about that. But in this world, there are two moons in the sky. In this world, the fates of two people, Tengo and Aomame, are closely intertwined. They are each, in their own way, doing something very dangerous. And in this world, there seems no way to save them both. Something extraordinary is starting.",,,,,,,,,,,1318,,,12.80 x 5.90 x 20.00 cm,,,English,,,,,,9780099578079,,0,0.000000,,Unread,,,,,,,,,,,,,,,2015/12/27 22:12:04.049736976,,,,,,,,Rp68AoCT4AAC,,0,,,,,
            //TODO: ...
        } catch (IOException e) {
            //TODO: ...
            throw new RuntimeException(e);
        }
    }

    private void importFromCodicesCSV(File file) {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();

        try {
            CSVReader csvReader = new CSVReaderBuilder(Files.newBufferedReader(file.toPath()))
                    .withSkipLines(0)
                    .withCSVParser(parser)
                    .build();

            //TODO: ...
        } catch (IOException e) {
            //TODO: ...
            throw new RuntimeException(e);
        }
    }

    private void importFromCodicesJSON(File file) {
        //TODO: ...
    }

    private void importFromCalibre(File file) {
        //TODO: ...
    }

    public void close() {
        getManager().closeCurrentStage();
    }
}
