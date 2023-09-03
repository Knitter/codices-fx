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
import javafx.stage.Stage;

public abstract class Controller {

    private ViewManager manager;
    private Stage stage;
    private String fxml;

    public Controller(ViewManager manager, String fxml) {
        this.manager = manager;
        this.fxml = fxml;
    }

    public ViewManager getManager() {
        return manager;
    }

    public String getFxml() {
        return fxml;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    abstract public String getTitle();
}
