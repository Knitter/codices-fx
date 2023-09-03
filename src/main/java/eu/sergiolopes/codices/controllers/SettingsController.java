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

import eu.sergiolopes.codices.view.ViewManager;
import javafx.fxml.FXML;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class SettingsController extends Controller {

    private Properties appSettings;

    @FXML
    private URL location;
    @FXML
    private ResourceBundle resources;
    public SettingsController(ViewManager vm) {
        this(vm, "settings.fxml");
    }

    public SettingsController(ViewManager vm, String fxml) {
        super(vm, fxml);
    }

    @Override
    public String getTitle() {
        return "Codices Settings";
    }

    @FXML
    private void initialize() {
        //TODO: ...
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