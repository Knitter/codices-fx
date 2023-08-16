package eu.sergiolopes.codices.controllers;

import eu.sergiolopes.codices.view.ViewManager;

public class MainController extends Controller {

    private ViewManager vm;

    public MainController(ViewManager vm, String fxml) {
        super(vm, fxml);
    }
}