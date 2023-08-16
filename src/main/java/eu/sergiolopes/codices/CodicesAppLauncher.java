package eu.sergiolopes.codices;

import eu.sergiolopes.codices.view.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class CodicesAppLauncher extends Application {

    // scene.setFill(Color.web("#f6f8fb"));
    // stage.setTitle("Codices");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ViewManager manager = new ViewManager();
        manager.showMainWindow();
    }

    @Override
    public void stop() throws Exception {
    }
}