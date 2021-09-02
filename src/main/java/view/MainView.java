package main.java.view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    public static final int WIDTH = 960;
    public static final int HEIGHT = 640;

    public MainView() throws HeadlessException {
        setUndecorated(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        add(new ServersView(), BorderLayout.CENTER);
        add(new ToolbarView(), BorderLayout.NORTH);

        pack();
        setBounds(0, 0, WIDTH, HEIGHT);
        setLocationRelativeTo(null);
    }
}
