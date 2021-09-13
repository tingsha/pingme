package main.java.view;

import main.java.controller.Controller;

public class SettingsView implements View{
    private Controller controller;

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
