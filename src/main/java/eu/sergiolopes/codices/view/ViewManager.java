package eu.sergiolopes.codices.view;

import eu.sergiolopes.codices.controllers.AuthorController;
import eu.sergiolopes.codices.controllers.Controller;
import eu.sergiolopes.codices.controllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
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
        Controller controller = new MainController(this, "main-view.fxml");
        initialize(controller);
    }

    private void initialize(Controller controller) {
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

        stage.setTitle(controller.getTitle());
        stage.setScene(scene);
        controller.setStage(stage);
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
        //TODO: Extract into reusable + share with this.initialize()
        var controller = new AuthorController(this, "authors.fxml");
        URL path = getClass().getResource("authors.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(path);
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
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(stages.get(0).getScene().getWindow());

        controller.setStage(stage);
        stage.show();
    }
}
