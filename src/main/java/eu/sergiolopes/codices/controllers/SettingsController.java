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

import eu.sergiolopes.codices.models.Format;
import eu.sergiolopes.codices.models.ItemType;
import eu.sergiolopes.codices.repositories.FormatRepository;
import eu.sergiolopes.codices.view.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class SettingsController extends Controller {

    private FormatRepository formatRepository;
    private Properties appSettings;

    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;
    @FXML
    private TextField audiobookFormats;
    @FXML
    private TextField ebookFormats;
    @FXML
    private TextField paperBookFormats;

    public SettingsController(ViewManager vm) {
        this(vm, "settings.fxml");
    }

    public SettingsController(ViewManager vm, String fxml) {
        super(vm, fxml);
        formatRepository = new FormatRepository(vm.getConnection());
    }

    @Override
    public String getTitle() {
        return "Codices Settings";
    }

    @FXML
    private void initialize() {
        List<String> ebookTypes = new ArrayList<>();
        List<String> audiobookTypes = new ArrayList<>();
        List<String> paperBookTypes = new ArrayList<>();
        for (Format f : formatRepository.findAll()) {
            if (f.getType().equals(ItemType.EBOOK.getType())) {
                ebookTypes.add(f.getName());
                continue;
            }

            if (f.getType().equals(ItemType.AUDIO_BOOK.getType())) {
                audiobookTypes.add(f.getName());
                continue;
            }

            if (f.getType().equals(ItemType.PAPER_BOOK.getType())) {
                paperBookTypes.add(f.getName());
            }
        }

        if (!ebookTypes.isEmpty()) {
            ebookFormats.setText(String.join(", ", ebookTypes));
        }

        if (!audiobookTypes.isEmpty()) {
            audiobookFormats.setText(String.join(", ", audiobookTypes));
        }

        if (!paperBookTypes.isEmpty()) {
            paperBookFormats.setText(String.join(", ", paperBookTypes));
        }
    }

    public void saveChanges() {
        try {
            appSettings.storeToXML(new FileOutputStream(getSettingsFile()), "Codices APP settings");
        } catch (IOException e) {
            //TODO: Proper error handling
            throw new RuntimeException(e);
        }
    }

    public void closeWindow() {
        getManager().closeCurrentStage();
    }

    private String getSettingsFile() {
        return System.getProperty("user.home") + System.getProperty("file.separator") + ".codices.xml";
    }

}