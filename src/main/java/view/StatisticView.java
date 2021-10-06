package main.java.view;

import main.java.controller.Controller;
import main.java.view.utils.Dragger;
import main.java.view.utils.ImageWriteEx;

import javax.swing.*;
import java.awt.*;

public class StatisticView extends JWindow implements View{
    private Controller controller;
    private final JTextField pingString = new JTextField(14);

    public StatisticView(){
        Dragger.dragWindow(pingString, this);
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);
        pingString.setText("connecting...");
        pingString.setForeground(Color.RED);
        pingString.setOpaque(false);
        pingString.setFont(new Font("Consolas", Font.PLAIN, 20));
        pingString.setEditable(false);
        pingString.setBackground(new Color(0, 0, 0, 0));
        pingString.setBorder(null);
        add(pingString, BorderLayout.WEST);
        setVisible(true);
        pack();
    }

    public void refresh(String ping){
        if (ping == null || ping.equals(""))
            return;
        pingString.setText(ping);
        ImageWriteEx.createImageWithText(32, 32, ping);
        try {
            Thread.sleep(1000);
            pingString.setText("");
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
