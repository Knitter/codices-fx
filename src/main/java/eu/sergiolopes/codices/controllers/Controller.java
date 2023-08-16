package eu.sergiolopes.codices.controllers;

import eu.sergiolopes.codices.view.ViewManager;

public abstract class Controller {
    protected ViewManager manager;
    private String fxml;

    public Controller(ViewManager manager, String fxml) {
        this.manager = manager;
        this.fxml = fxml;
    }

    public String getFxml() {
        return fxml;
    }
}
