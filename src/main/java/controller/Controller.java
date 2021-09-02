package main.java.controller;

import main.java.model.Model;
import main.java.view.MainView;

public class Controller {
    private final Model model;
    private final MainView view;

    public Controller(Model model, MainView view) {
        this.model = model;
        this.view = view;
    }
}
