package eu.sergiolopes.codices.view;

import eu.sergiolopes.codices.controllers.Controller;
import eu.sergiolopes.codices.controllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ViewManager {

    private ArrayList<Stage> stages;

    public ViewManager() {
        stages = new ArrayList<Stage>();
    }

    public void showMainWindow() {
        Controller controller = new MainController(this, "main-view.fxml");
        initialize(controller);
        //mainViewInitialized = true;
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

        Scene scene = new Scene(parent);
        updateStyle(scene);

        Stage stage = new Stage();
        stages.add(stage);

        stage.setScene(scene);
        stage.show();
    }

    //public void closeStage(Stage stageToClose) {
    //    stageToClose.close();
    //    //activeStages.remove(stageToClose);
    //}

    //public void updateAllStyles() {
    //for (Stage stage : activeStages) {
    //    Scene scene = stage.getScene();
    //    updateStyle(scene);
    //}
    //}

    private void updateStyle(Scene scene) {
        //    scene.getStylesheets().clear();
        //scene.getStylesheets().add(getClass().getResource("/eu/sergiolopes/codices/css/default.css").toExternalForm());
        //    scene.getStylesheets().add(getClass().getResource(ColorTheme.getCssPath(colorTheme)).toExternalForm());
        //    scene.getStylesheets().add(getClass().getResource(FontSize.getCssPath(fontSize)).toExternalForm());
    }

}
