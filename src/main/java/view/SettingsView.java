package main.java.view;

import main.java.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class SettingsView extends JWindow implements View {
    private Controller controller;

    public SettingsView(){
        setPreferredSize(new Dimension(300, 400));
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
