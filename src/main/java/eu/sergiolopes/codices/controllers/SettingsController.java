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