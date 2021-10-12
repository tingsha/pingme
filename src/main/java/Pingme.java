package main.java;

import main.java.controller.Controller;
import main.java.model.Model;
import main.java.view.MainView;

import java.io.IOException;

public class Pingme {
    public static void main(String[] args) throws IOException {
        Model model = new Model();
        Controller controller = new Controller(model);
        MainView mainView = new MainView(controller);
    }
}
