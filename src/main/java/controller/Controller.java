package main.java.controller;

import main.java.model.Model;
import main.java.view.MainView;
import main.java.view.ServersView;
import main.java.view.SettingsView;
import main.java.view.ToolbarView;

public class Controller {
    private final Model model;
    private MainView view;


    public Controller(Model model) {
        this.model = model;
    }

    public void onClickSettingsBtn(ServersView.ServerButton button){
        model.setDomain(button.getDomain());
    }
}
