package main.java.view;

import main.java.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class ServersView extends JPanel implements View{
    private Controller controller;

    public ServersView(){
        setBackground(Color.PINK);
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
