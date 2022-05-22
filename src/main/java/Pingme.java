package main.java;

import main.java.controller.Controller;
import main.java.model.Model;
import main.java.view.MainView;

public class Pingme {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        new MainView(controller);
    }
}
