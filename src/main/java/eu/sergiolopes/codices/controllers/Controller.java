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
