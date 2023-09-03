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
package eu.sergiolopes.codices.view;

import eu.sergiolopes.codices.controllers.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;

public class ViewManager {

    //TODO: Me no liky! improve manager pattern, will lead to lots of "showXYZ" methods to manage windows/interaction.
    //TODO: More styles
    //Ex: scene.setFill(Color.web("#f6f8fb"));

    private Connection connection;
    private ArrayList<Stage> stages;

    public ViewManager(Connection connection) {
        this.connection = connection;
        stages = new ArrayList<>();
    }

    public Connection getConnection() {
        return connection;
    }

    public void showMainWindow() {
        Controller controller = new MainController(this);
        initialize(controller, true);
    }

    private void initialize(Controller controller, boolean fullScreen) {
        URL path = getClass().getResource(controller.getFxml());

        FXMLLoader fxmlLoader = new FXMLLoader(path);
        fxmlLoader.setController(controller);
        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //TODO: Cleanup and improve param handling and controller setup
        Scene scene = new Scene(parent);
        applyStylesTo(scene);

        Stage stage = new Stage();
        stages.add(stage);
        controller.setStage(stage);

        if (fullScreen) {
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
        }

        stage.setMaximized(fullScreen);
        stage.setTitle(controller.getTitle());
        stage.setScene(scene);
        stage.show();
    }

    public Stage getCurrentStage() {
        if (stages.isEmpty()) {
            return null;
        }

        return stages.get(stages.size() - 1);
    }

    public void closeCurrentStage() {
        closeStage(stages.size() - 1);
    }

    public void closeStage(int idx) {
        if (idx >= 0 && idx < stages.size()) {
            closeStage(stages.get(idx));
        }
    }

    public void closeStage(Stage stage) {
        if (stages.isEmpty() || stage == null) {
            return;
        }

        stage.close();
        stages.remove(stage);
    }

    public void applyStylesToAll() {
        for (Stage stage : stages) {
            applyStylesTo(stage.getScene());
        }
    }

    private void applyStylesTo(Scene scene) {
        //scene.getStylesheets().clear();
        //scene.getStylesheets().add(getClass().getResource("/eu/sergiolopes/codices/css/default.css").toExternalForm());
        //scene.getStylesheets().add(getClass().getResource(ColorTheme.getCssPath(colorTheme)).toExternalForm());
        //scene.getStylesheets().add(getClass().getResource(FontSize.getCssPath(fontSize)).toExternalForm());
    }

    public void showAuthorWindow() {
        showModal(new AuthorController(this));
    }

    public void showCollectionsWindow() {
        showModal(new CollectionController(this));
    }

    public void showPublishersWindow() {
        showModal(new PublisherController(this));
    }

    public void showSeriesWindow() {
        showModal(new SeriesController(this));
    }

    public void showSettingsWindow() {
        showModal(new SettingsController(this));
    }

    public void showEbookWindow() {
        //TODO: ...
    }

    public void showAudioBookWindow() {
        //TODO: ...
    }

    public void showPaperBookWindow() {
        showModal(new BookController(this));
    }

    public void showImportDialog() {
        showModal(new ImportDialogController(this));
    }

    private void showModal(Controller controller) {
        //TODO: share with this.initialize()
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getFxml()));
        fxmlLoader.setController(controller);
        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(parent);
        applyStylesTo(scene);

        Stage stage = new Stage();
        stages.add(stage);

        stage.setTitle(controller.getTitle());
        stage.setScene(scene);
        //TODO: stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(stages.get(0).getScene().getWindow());

        controller.setStage(stage);
        stage.show();
    }
}
